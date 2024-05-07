package com.example.sobes.controller;

import com.example.sobes.service.TelegramBot;
import com.example.sobes.service.TelegramMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

@Component
public class SocketTextHandler extends TextWebSocketHandler implements WebSocketHandler {
    private WebSocketSession webSocketSession;

    private final ApplicationEventPublisher eventPublisher;

    public SocketTextHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

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
        System.out.println("Text:" + text[0] + " id: " + text[1]);
        String answer = "There is no '" + TARGET_CHAR + "' in the line";;
        if (text[0].contains(TARGET_CHAR))
            answer = "The line contains '" + TARGET_CHAR + "'";

        TelegramMessageEvent event = new TelegramMessageEvent(this, Long.parseLong(text[1]), answer);
        eventPublisher.publishEvent(event);

    }
}