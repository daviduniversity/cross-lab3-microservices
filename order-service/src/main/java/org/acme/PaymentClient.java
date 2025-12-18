package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces; // <--- Додай імпорт
import jakarta.ws.rs.core.MediaType; // <--- Додай імпорт
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "payment-api")
@Path("/payment")
public interface PaymentClient {

    @GET
    @Path("/{user}/{amount}")
    @Produces(MediaType.TEXT_PLAIN) // <--- ЦЕ ВИПРАВИТЬ ПОМИЛКУ 406
    Uni<String> pay(@PathParam("user") String user, @PathParam("amount") Double amount);
}