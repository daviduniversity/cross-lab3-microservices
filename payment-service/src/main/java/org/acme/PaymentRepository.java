package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<PaymentEntity> {
    // Цей клас автоматично отримує методи: persist, listAll, findById тощо.
    // Тобі не треба їх писати вручну!
}