package com.example.sobes.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.WebSocketHandler;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@PropertySource("application.properties")
public class WebSocketClientExample {

    @Value("${websocket.url}")
    private String webSocketServerUrl;

    private WebSocketConnectionManager connectionManager;

    private final WebSocketHandler webSocketHandler;

    public WebSocketClientExample(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostConstruct
    public void connect() {
        try {
            URI serverUri = new URI(webSocketServerUrl);
            StandardWebSocketClient client = new StandardWebSocketClient();
            this.connectionManager = new WebSocketConnectionManager(client, webSocketHandler, serverUri.toString());
            this.connectionManager.start();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to establish WebSocket connection", e);
        }
    }

    @PreDestroy
    public void disconnect() {
        if (this.connectionManager != null) {
            this.connectionManager.stop();
        }
    }
}