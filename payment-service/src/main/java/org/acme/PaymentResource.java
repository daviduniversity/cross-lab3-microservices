package org.acme;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/payment")
public class PaymentResource {

    @Inject
    PaymentRepository repository; // Впроваджуємо наш репозиторій

    // Метод оплати (який викликає Order Service)
    @GET
    @Path("/{user}/{amount}")
    @Produces(MediaType.TEXT_PLAIN)
    @WithTransaction // Відкриваємо транзакцію для запису
    public Uni<String> pay(@PathParam("user") String user, @PathParam("amount") Double amount) {
        PaymentEntity payment = new PaymentEntity();
        payment.userId = user;
        payment.amount = amount;
        payment.status = "PAID";

        // Використовуємо РЕПОЗИТОРІЙ для збереження
        return repository.persist(payment)
                .onItem().transform(saved -> "PAID (Transaction ID: " + saved.id + ")");
    }

    // Метод для перевірки (щоб ми бачили список платежів у браузері)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<PaymentEntity>> getAllPayments() {
        return repository.listAll();
    }
}