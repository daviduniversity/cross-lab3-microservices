package org.acme;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.acme.inventory.InventoryGrpc;
import org.acme.inventory.StockRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/order")
public class OrderResource {

    @GrpcClient("inventory-client") // Підключаємося до Складу (gRPC)
    InventoryGrpc inventoryService;

    @Inject
    @RestClient // Підключаємося до Банку (REST)
    PaymentClient paymentClient;

    @GET
    @Path("/{item}")
    public Uni<String> createOrder(@PathParam("item") String item) {
        // 1. Питаємо склад
        return inventoryService.checkStock(StockRequest.newBuilder().setItemName(item).build())
                .onItem().transformToUni(stock -> { // <--- Змінили transform на transformToUni

                    if (stock.getExists()) {
                        // 2. Якщо є - платимо (тепер це асинхронний виклик)
                        return paymentClient.pay("User1", 100.0)
                                .onItem().transform(bankResponse -> {
                                    // 3. Коли банк відповість - формуємо фінальну відповідь
                                    return "Success! Item '" + item + "' ordered. Stock left: " + stock.getQuantity() + ". Status: " + bankResponse;
                                });
                    } else {
                        // Якщо товару немає - повертаємо просту відповідь, загорнуту в Uni
                        return Uni.createFrom().item("Failed! Item '" + item + "' is out of stock.");
                    }
                });
    }
}