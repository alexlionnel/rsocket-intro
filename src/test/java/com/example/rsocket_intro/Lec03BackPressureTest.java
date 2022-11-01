package com.example.rsocket_intro;

import com.example.rsocket_intro.dto.RequestDto;
import com.example.rsocket_intro.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03BackPressureTest {

    private RSocket rSocket;

    @BeforeAll
    public void setup() {
        this.rSocket = RSocketConnector
                .create()
                .connect(TcpClientTransport.create("localhost", 6565))
                .block();
    }

    @Test
    public void backPressure() {
        Payload payload = ObjectUtil.toPayload(new RequestDto(5));
        Flux<String> flux = this.rSocket.requestStream(DefaultPayload.create(""))
                .map(Payload::getDataUtf8)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectNextCount(1000)
                .verifyComplete();
    }
}