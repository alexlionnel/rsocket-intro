package com.example.rsocket_intro.service;

import com.example.rsocket_intro.dto.RequestDto;
import com.example.rsocket_intro.dto.ResponseDto;
import com.example.rsocket_intro.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BatchJobService implements RSocket {

    private RSocket rSocket;

    public BatchJobService(RSocket rSocket) {
        this.rSocket = rSocket;
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
        System.out.println("Received : " + requestDto);

        Mono.just(requestDto)
                .delayElement(Duration.ofSeconds(10))
                .doOnNext(i -> System.out.println("Emitting"))
                .flatMap(this::findCube)
                .subscribe();

        return Mono.empty();
    }

    private Mono<Void> findCube(RequestDto requestDto) {
        int input = requestDto.getInput();
        int output = input * input * input;
        ResponseDto responseDto = new ResponseDto(input, output);
        Payload payload = ObjectUtil.toPayload(responseDto);
        return this.rSocket.fireAndForget(payload);
    }
}
