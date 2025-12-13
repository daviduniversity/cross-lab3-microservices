package org.acme;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.acme.inventory.InventoryGrpc;
import org.acme.inventory.StockRequest;
import org.acme.inventory.StockResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class InventoryService implements InventoryGrpc {
    // Фейкова база даних
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    public InventoryService() {
        stock.put("laptop", 5); // У нас є 5 ноутбуків
        stock.put("phone", 0);  // Телефонів немає
    }

    @Override
    public Uni<StockResponse> checkStock(StockRequest request) {
        Integer quantity = stock.getOrDefault(request.getItemName(), 0);
        return Uni.createFrom().item(StockResponse.newBuilder()
                .setExists(quantity > 0)
                .setQuantity(quantity)
                .build());
    }
}