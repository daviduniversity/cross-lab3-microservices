package org.acme;

import io.smallrye.mutiny.Uni; // <--- Додали імпорт
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "payment-api")
@Path("/payment")
public interface PaymentClient {

    @GET
        // Змінили String на Uni<String>
    Uni<String> pay(@QueryParam("user") String user, @QueryParam("amount") double amount);
}