package com.example.sobes.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class SocketTextHandler extends TextWebSocketHandler implements WebSocketHandler {
    private WebSocketSession webSocketSession;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.webSocketSession = session; // Сохраняем сессию для последующей отправки сообщений
    }

    public void send(String text) throws IOException {
        if (this.webSocketSession != null) {
            this.webSocketSession.sendMessage(new TextMessage(text));
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String TARGET_CHAR = String.valueOf(',');
        String answer = "There is no '" + TARGET_CHAR + "' in the line";;
        if (message.getPayload().contains(TARGET_CHAR))
            answer = "The line contains ','";
        // тут answer должен отпралвяться опять в класс TelegramBot, в метод checkThis
    }
}