package com.example.sobes.service;

import com.example.sobes.config.BotConfig;
import com.example.sobes.config.WebSocketClientExample;
import com.example.sobes.controller.SocketTextHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private SocketTextHandler socketTextHandler;
    final BotConfig botConfig;

    static final String HELP_TEXT = "This bot created to demonstration.\n\n" +
            "You can execute commands from the main menu.\n\n" +
            "Type /start to greet the bot\n" +
            "Type /checkthis to check list for symbol\n" +
            "Type /timer to start timer\n" +
            "Type /help to show this help message";

    private WebSocketClientExample wsClient;


    public TelegramBot(BotConfig botConfig, SocketTextHandler socketTextHandler) {

            this.socketTextHandler = socketTextHandler;
            this.botConfig = botConfig;
            wsClient = new WebSocketClientExample(socketTextHandler);
            wsClient.connect();

        //меню
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/checkthis", "check list for symbol"));
        listOfCommands.add(new BotCommand("/timer", "start timer"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    public void checkThis(long chatId, String text) {
        // Отправляем сообщение через WebSocket
        try {
            socketTextHandler.send(text + "\u200B" + chatId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    // что должен делать бот, когда ему пишут
    @Override
    public void onUpdateReceived(Update update) {
        // проверка, что текст есть
        if (update.hasMessage() && update.getMessage().hasText()) {
            // обработка сообщения
            String message = update.getMessage().getText();
            // боту необходимо id переписки, чтобы общаться с множеством людей
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();

            if (message.equals("/start")) {
                startCommandReceived(chatId, userName);
            } else if (message.equals("/help")) {
                sendMessage(chatId, HELP_TEXT);
                log.info(userName + " displayed a list of commands");
            } else if (message.startsWith("/checkthis")) {
                log.info(userName + " checked the array...");
                checkThis(chatId, message.substring(10));
            } else if (message.startsWith("/timer")) {
                try {
                    int n = Integer.parseInt(message.substring(7));
                    customTimer(chatId, n);
                    log.info(userName + " started the timer for " + n + " seconds");
                } catch (InterruptedException e) {
                    sendMessage(chatId, "incorrect time value");
                    throw new RuntimeException(e);
                }
            } else {
                sendMessage(chatId, "Command not found");
                log.info(userName + " entered a non-existent command");
            }

        }

    }
    private void customTimer(long chatId, int n) throws NumberFormatException, InterruptedException {
        for (int i = 1; i <= n; i++) {
            Thread.sleep(1000);
            sendMessage(chatId, String.valueOf(i));
        }
    }

    private void startCommandReceived(long chatId, String name){
        String answer = "Hi, " + name + ", nice to meet you!";
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    public void sendMessage(long chatId, String textToSand) {
        // класс SendMessage позволяет послать сообщение
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSand);

        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    @EventListener
    public void onTelegramMessageEvent(TelegramMessageEvent event) {
        sendMessage(event.getChatId(), event.getMessage());
    }

}
