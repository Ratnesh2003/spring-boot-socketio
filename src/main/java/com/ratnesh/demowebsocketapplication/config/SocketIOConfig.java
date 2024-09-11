package com.ratnesh.demowebsocketapplication.config;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketIOConfig {

    // I have set the configuration values in application.yaml file
    @Value("${socket.host}")
    private String socketHost;
    @Value("${socket.port}")
    private int socketPort;

    // SocketIOServer class is used to create a socket server
    private SocketIOServer server;

    // This method returns a SocketIOServer instance
    @Bean
    public SocketIOServer socketIOServer() {
        // Configuration object holds the server settings
        Configuration config = new Configuration();

        config.setHostname(socketHost);
        config.setPort(socketPort);

        // Authorization listener
        config.setAuthorizationListener(data -> {
            String token = data.getHttpHeaders().get("User-Name");
            if (!token.isEmpty()) {
                // You can extract user information from token using your JWTTokenUtil class and validate it
                // or throw error if token is invalid
                // you can pass more information in headers like role, email, etc.
                // data object can be used to add custom headers after authorization
                data.getHttpHeaders().add("User", "userDetailsAfterAuthorization");
                return new AuthorizationResult(true);
            }
            return new AuthorizationResult(false);
        });

        server = new SocketIOServer(config);
        server.start();

        server.addConnectListener(client -> log.info("Client connected: {}", client.getSessionId()));
        server.addDisconnectListener(client -> log.info("Client disconnected: {}", client.getSessionId()));

        return server;
    }

    @PreDestroy
    public void stopSocketServer() {
        this.server.stop();
    }
}
