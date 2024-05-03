
package com.example.sobes.config;

import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint(subprotocols = "/websocket")
public class WebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        session.getAsyncRemote().sendText("Hello, WebSocket server!");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    public static void connectToWebSocket() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = URI.create("ws://localhost:8081/websocket");
            container.connectToServer(WebSocketClient.class, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

