package com.example.rsocket_intro;

import com.example.rsocket_intro.service.SocketAcceptorImpl;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class Server {

    public static void main(String[] args) {
        RSocketServer rSocketServer = RSocketServer.create(new SocketAcceptorImpl());
        CloseableChannel closeableChannel = rSocketServer.bindNow(TcpServerTransport.create(6565));

        // keep listening
        closeableChannel.onClose().block();
    }
}
