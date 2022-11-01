package com.example.rsocket_intro;

import com.example.rsocket_intro.dto.RequestDto;
import com.example.rsocket_intro.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04PersistentConnectionTest {

    private RSocketClient rSocketClient;

    @BeforeAll
    public void setup() {
        Mono<RSocket> socketMono = RSocketConnector
                .create()
                .connect(TcpClientTransport.create("localhost", 6565))
                .doOnNext(r -> System.out.println("Going to connect"));

        this.rSocketClient = RSocketClient.from(socketMono);
    }

    @Test
    public void connectionTest() throws InterruptedException {
        Flux<String> flux1 = this.rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
                .map(Payload::getDataUtf8)
                .delayElements(Duration.ofMillis(300))
                .take(10)
                .doOnNext(System.out::println);

        StepVerifier.create(flux1)
                .expectNextCount(10)
                .verifyComplete();

        System.out.println("Going to sleep");
        Thread.sleep(15000);
        System.out.println("Woke up");

        Flux<String> flux2 = this.rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
                .map(Payload::getDataUtf8)
                .delayElements(Duration.ofMillis(300))
                .take(10)
                .doOnNext(System.out::println);

        StepVerifier.create(flux2)
                .expectNextCount(10)
                .verifyComplete();
    }
}
