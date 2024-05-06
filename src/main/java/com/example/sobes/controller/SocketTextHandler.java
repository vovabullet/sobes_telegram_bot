package com.example.sobes.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
        String[] text = message.getPayload().split("\u200B");
        String answer = "There is no '" + TARGET_CHAR + "' in the line";;
        if (text[0].contains(TARGET_CHAR))
            answer = "The line contains '" + TARGET_CHAR + "' in the line";

        sendMessage(Long.parseLong(text[1]), answer);

    }

    public void sendMessage(long chatId, String textToSand) {
        // класс SendMessage позволяет послать сообщение
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSand);
    }
}