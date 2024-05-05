package com.example.sobes.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BotInitializer {

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            WebSocketClient.connectToWebSocket(); // Подключение к веб-сокету
        } catch (Exception e) {
            log.error("Error connecting to WebSocket: " + e.getMessage());
        }
    }
}