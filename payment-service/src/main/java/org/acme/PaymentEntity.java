package org.acme;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue
    public Long id;

    public String userId;
    public Double amount;
    public String status; // наприклад, "PAID"
}