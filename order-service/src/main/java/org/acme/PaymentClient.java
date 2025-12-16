package org.acme;

import io.quarkus.oidc.token.propagation.AccessToken; // <--- Має перестати бути червоним
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "payment-api")
@AccessToken // <--- Це анотація, яка передає токен
@Path("/payment")
public interface PaymentClient {
    @GET
    Uni<String> pay(@QueryParam("user") String user, @QueryParam("amount") double amount);
}