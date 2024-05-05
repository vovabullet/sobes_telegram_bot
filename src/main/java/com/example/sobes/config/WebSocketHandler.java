package com.example.sobes.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final String HELP_TEXT = "This bot created to demonstration.\n\n" +
            "You can execute commands from the main menu.\n\n" +
            "Type /start to greet the bot\n" +
            "Type /listcheck to check the list\n" +
            "Type /help to show this help message";

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String messageText = message.getPayload();

        switch (messageText) {
            case "/start":
                startCommandReceived(session);
                break;
            case "/help":
                sendMessage(session, HELP_TEXT);
                break;
            case "/listcheck":
                listCheckCommandReceived(session);
                break;
            case "/timer":
                startSendingMessagesEverySecond(session);
                break;
            default:
                sendMessage(session, "Command not found");
        }
    }

    private void startCommandReceived(WebSocketSession session) throws Exception {
        String answer = "Hi, nice to meet you!";
        sendMessage(session, answer);
    }

    private void listCheckCommandReceived(WebSocketSession session) throws Exception {
        String[] array = { ".", ".", ".", ",", "."};
        for (String item : array) {
            if (item.contains(",")) {
                sendMessage(session, "character ',' found in the list");
                return;
            }
        }
        sendMessage(session, "No ',' character found in the list");
    }

    private void sendMessage(WebSocketSession session, String textToSend) throws Exception {
        session.sendMessage(new TextMessage(textToSend));
    }

    // открывается веб-сокет сессия
    private void startSendingMessagesEverySecond(WebSocketSession session) {
        new Thread(() -> {
            try {
                for (int i = 0; i < 60; i++) { // 60 сообщений (1 минута)
                    if (session.isOpen()) {
                        sendMessage(session, "Message " + i);
                        Thread.sleep(1000);
                    } else {
                        break; // Прекратить отправку, если сессия закрылась
                    }
                }
            } catch (InterruptedException | IOException e) {
                // Обработка исключения
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}