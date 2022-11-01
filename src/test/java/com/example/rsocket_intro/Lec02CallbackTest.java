package com.example.rsocket_intro;

import com.example.rsocket_intro.client.CallbackService;
import com.example.rsocket_intro.dto.RequestDto;
import com.example.rsocket_intro.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02CallbackTest {

    private RSocket rSocket;

    @BeforeAll
    public void setup() {
        this.rSocket = RSocketConnector
                .create()
                .acceptor(SocketAcceptor.with(new CallbackService()))
                .connect(TcpClientTransport.create("localhost", 6565))
                .block();
    }

    @Test
    public void callback() throws InterruptedException {
        Payload payload = ObjectUtil.toPayload(new RequestDto(5));
        Mono<Void> mono = this.rSocket.fireAndForget(payload);

        StepVerifier.create(mono)
                .verifyComplete();

        System.out.println("Going to wait");

        Thread.sleep(12000);
    }
}
