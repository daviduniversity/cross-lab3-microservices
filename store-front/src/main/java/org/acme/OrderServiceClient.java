package org.acme;

import io.quarkus.oidc.token.propagation.AccessToken; // Важливий імпорт!
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "order-api")
@AccessToken // <--- МАГІЯ: Передає токен далі автоматично
@Path("/order")
public interface OrderServiceClient {

    @GET
    @Path("/{item}")
    Uni<String> orderItem(@PathParam("item") String item);
}