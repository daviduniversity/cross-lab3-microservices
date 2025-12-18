package org.acme;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/inventory") // Доступ буде за адресою http://localhost:8081/inventory
public class InventoryResource {

    @GET
    @PermitAll // Дозволяємо перегляд без авторизації (для тестів)
    @WithSession // Відкриваємо сесію до БД
    public Uni<List<InventoryEntity>> getAllItems() {
        // Active Record: метод listAll() повертає всі записи з таблиці
        return InventoryEntity.listAll();
    }
}