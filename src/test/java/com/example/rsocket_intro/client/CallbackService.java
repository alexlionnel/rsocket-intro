package com.example.rsocket_intro.client;

import com.example.rsocket_intro.dto.ResponseDto;
import com.example.rsocket_intro.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Mono;

public class CallbackService implements RSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("CLient received : " + ObjectUtil.toObject(payload, ResponseDto.class));
        return Mono.empty();
    }
}
