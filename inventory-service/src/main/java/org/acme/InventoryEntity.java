package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import io.smallrye.mutiny.Uni;

@Entity
public class InventoryEntity extends PanacheEntity {
    public String name;
    public int quantity;

    // Метод пошуку
    public static Uni<InventoryEntity> findByName(String name) {
        return find("name", name).firstResult();
    }
}

// для бд
// docker exec -it <ID_КОНТЕЙНЕРА_INVENTORY> psql -U quarkus -d quarkus
// SELECT * FROM InventoryEntity;