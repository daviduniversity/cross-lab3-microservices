package org.acme;

import io.grpc.*;
import io.quarkus.grpc.GlobalInterceptor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@GlobalInterceptor
public class GrpcTokenInterceptor implements ClientInterceptor {

    @Inject
    JsonWebToken jwt;

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String token = jwt.getRawToken();

                // --- ЛОГУВАННЯ (Діагностика) ---
                if (token == null) {
                    System.out.println(">>> УВАГА! Токен в Order Service = NULL. Я анонім.");
                } else {
                    System.out.println(">>> УСПІХ! Токен є: " + token.substring(0, 10) + "...");
                    headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + token);
                }
                // -------------------------------

                super.start(responseListener, headers);
            }
        };
    }
}