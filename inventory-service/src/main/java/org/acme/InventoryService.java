package org.acme;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.acme.inventory.InventoryGrpc;
import org.acme.inventory.StockRequest;
import org.acme.inventory.StockResponse;
import jakarta.annotation.security.PermitAll; // Дозволити всім
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
@PermitAll  // <--- ГОЛОВНА ЗМІНА: Ми пускаємо всіх, навіть без токена
public class InventoryService implements InventoryGrpc {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    public InventoryService() {
        stock.put("laptop", 5);
        stock.put("phone", 0);
    }

    @Override
    public Uni<StockResponse> checkStock(StockRequest request) {
        // Просто бізнес-логіка, без перевірок безпеки
        Integer quantity = stock.getOrDefault(request.getItemName(), 0);

        System.out.println("✅ INVENTORY: Запит отримано для " + request.getItemName());

        return Uni.createFrom().item(StockResponse.newBuilder()
                .setExists(quantity > 0)
                .setQuantity(quantity)
                .build());
    }
}