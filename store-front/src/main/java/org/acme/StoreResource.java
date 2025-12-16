package org.acme;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
@Authenticated // Вимагаємо вхід для всього сайту
public class StoreResource {

    @Inject
    Template store; // Це наш store.html

    @Inject
    JsonWebToken jwt; // Дані про користувача

    @Inject
    @RestClient
    OrderServiceClient orderClient;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return store.data("username", jwt.getName()).data("result", "Ready");
    }

    @POST
    @Path("/store/order")
    @Produces(MediaType.TEXT_HTML)
    public Uni<TemplateInstance> order(@FormParam("item") String item) {
        return orderClient.orderItem(item)
                .onItem().transform(status ->
                        store.data("username", jwt.getName()).data("result", status)
                );
    }
}