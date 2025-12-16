package org.acme;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/payment")
@Authenticated
public class PaymentResource {

    @GET
    public String pay(@QueryParam("user") String user, @QueryParam("amount") double amount) {
        System.out.println("Payment processing for " + user);
        return "PAID"; // Банк каже "Оплачено"
    }
}