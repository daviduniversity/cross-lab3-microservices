package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders") // "Order" - зарезервоване слово в SQL, тому змінюємо ім'я таблиці
public class OrderEntity extends PanacheEntity {
    public String userId;
    public String item;
    public double price;
}