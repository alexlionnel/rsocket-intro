package com.example.rsocket_intro.service;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import reactor.core.publisher.Mono;

public class SocketAcceptorImpl implements SocketAcceptor {

    // appelé une fois lors de la connexion entre les deux parties
    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload connectionSetupPayload, RSocket rSocket) {
        System.out.println("SocketAcceptorImpl accept method");

        if (isValidClient(connectionSetupPayload.getDataUtf8())) {
            return Mono.just(new MathService());
        } else {
            return Mono.just(new FreeService());
        }

        // return Mono.fromCallable(MathService::new);
        // return Mono.fromCallable(() -> new BatchJobService(rSocket));
        // return Mono.fromCallable(FastProducerService::new);
    }

    private boolean isValidClient(String credentials) {
        return "user:password".equals(credentials);
    }
}
