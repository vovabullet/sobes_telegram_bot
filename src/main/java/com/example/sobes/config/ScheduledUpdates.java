/*
package com.example.sobes.config;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// Для периодической отправки сообщений использую Scheduled Updates из Spring
@Component
public class ScheduledUpdates {

    private final SimpMessagingTemplate messagingTemplate;

    public ScheduledUpdates(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void sendUpdates() {
        // Здесь логика для формирования вашего сообщения
        String message = "every second message";
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}


 */