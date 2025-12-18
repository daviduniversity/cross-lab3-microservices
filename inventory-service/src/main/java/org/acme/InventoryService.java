package org.acme;

import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction; // <--- УВАГА НА ЦЕЙ ІМПОРТ
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import org.acme.inventory.InventoryGrpc;
import org.acme.inventory.StockRequest;
import org.acme.inventory.StockResponse;

@GrpcService
@PermitAll
public class InventoryService implements InventoryGrpc {

    @Override
    @WithTransaction // <--- БУЛО @WithSession, МАЄ БУТИ @WithTransaction
    public Uni<StockResponse> checkStock(StockRequest request) {
        return InventoryEntity.findByName(request.getItemName())
                .onItem().ifNotNull().call(item -> {
                    // Якщо товар є і його більше 0 - списуємо
                    if (item.quantity > 0) {
                        item.quantity = item.quantity - 1;
                        return item.persist(); // Зберігаємо зміну в базі
                    }
                    return Uni.createFrom().voidItem();
                })
                .map(item -> {
                    // Формуємо відповідь (true, якщо товару вистачило)
                    boolean exists = item.quantity >= 0;
                    return StockResponse.newBuilder()
                            .setExists(exists)
                            .setQuantity(item.quantity)
                            .build();
                })
                // Якщо товару взагалі немає в базі (null)
                .onItem().ifNull().continueWith(() ->
                        StockResponse.newBuilder()
                                .setExists(false)
                                .setQuantity(0)
                                .build()
                );
    }
}