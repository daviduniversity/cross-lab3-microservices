package org.acme;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.acme.inventory.InventoryGrpc;
import org.acme.inventory.StockRequest;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
import jakarta.annotation.security.PermitAll;

@Path("/order")
@Authenticated
public class OrderResource {

    @GrpcClient("inventory-client") // Підключаємося до Складу (gRPC)
    InventoryGrpc inventoryService;

    @Inject
    @RestClient // Підключаємося до Банку (REST)
    PaymentClient paymentClient;

    @Inject
    JsonWebToken jwt; // Для отримання імені користувача

    @GET
    @Path("/{item}")
    @WithTransaction // <--- ВАЖЛИВО! Відкриває транзакцію для запису в БД
    public Uni<String> createOrder(@PathParam("item") String item) {
        // Отримуємо ім'я користувача (якщо є токен) або ставимо заглушку
        String userId = (jwt.getName() != null) ? jwt.getName() : "User1";

        // 1. Питаємо склад
        return inventoryService.checkStock(StockRequest.newBuilder().setItemName(item).build())
                .onItem().transformToUni(stock -> {

                    if (stock.getExists()) {
                        // 2. Якщо є - платимо
                        return paymentClient.pay(userId, 100.0)
                                .onItem().transformToUni(bankResponse -> { // <--- Тут теж transformToUni, бо persist повертає Uni

                                    // 3. Створюємо запис для бази даних (Active Record)
                                    OrderEntity order = new OrderEntity();
                                    order.userId = userId;
                                    order.item = item;
                                    order.price = 100.0;

                                    // 4. Зберігаємо в базу (.persist()) і формуємо відповідь
                                    return order.persist()
                                            .onItem().transform(savedOrder ->
                                                    "Success! Item '" + item + "' ordered & saved to DB (ID: " + order.id + "). " +
                                                            "Stock left: " + stock.getQuantity() + ". Bank: " + bankResponse
                                            );
                                });
                    } else {
                        // Якщо товару немає - повертаємо просту відповідь
                        return Uni.createFrom().item("Failed! Item '" + item + "' is out of stock.");
                    }
                });
    }
    @GET
    @PermitAll
    public Uni<List<OrderEntity>> getAllOrders() {
        // Active Record метод: "Дай мені всі записи з таблиці"
        return OrderEntity.listAll();
    }
}