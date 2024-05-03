package com.example.sobes.config;

import com.example.sobes.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j //создание логов
@Component
public class BotInitializer {

    @Autowired
    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class}) //автовызов метода, когда происходит осбытие
    public void init() throws TelegramApiException {
        // объект для взаимодействия с Telegram Bot API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
            WebSocketClient.connectToWebSocket(); //коннект к веб сокету
        } catch (TelegramApiException e) {
            // логи
            log.error("Error: " + e.getMessage());
        }
    }


}
