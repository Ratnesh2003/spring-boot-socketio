package com.ratnesh.demowebsocketapplication.controller;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.ratnesh.demowebsocketapplication.model.SocketDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketController {
    protected final SocketIOServer socketServer;

    public WebSocketController(SocketIOServer socketServer) {
        this.socketServer = socketServer;
        this.socketServer.addEventListener("demoEvent", SocketDetail.class, demoEvent);
    }


    public DataListener<SocketDetail> demoEvent = new DataListener<>() {
        @Override
        public void onData(SocketIOClient client, SocketDetail socketDetail, AckRequest ackRequest) {
            log.info("Demo event received: {}", socketDetail);
            String userInfo = client.getHandshakeData().getHttpHeaders().get("User-Name");
            log.info("User info: {}", userInfo);
            // Add your business logic here.
            ackRequest.sendAckData("Demo event received");
        }
    };
}
