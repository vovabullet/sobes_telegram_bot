package com.example.sobes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
public class TelegramMessageEvent extends ApplicationEvent {
    private final long chatId;
    private final String message;

    public TelegramMessageEvent(Object source, long chatId, String message) {
        super(source);
        this.chatId = chatId;
        this.message = message;
    }

    public long getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }
}
