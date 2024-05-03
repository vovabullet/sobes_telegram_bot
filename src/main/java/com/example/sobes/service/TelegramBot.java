package com.example.sobes.service;

import com.example.sobes.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    static final String HELP_TEXT = "This bot created to demonstration.\n\n" +
            "You can execute commands from the main menu.\n\n" +
            "Type /start to greet the bot\n" +
            "Type /help to show this help message";

    // menu
    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
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

            switch (message) {
                case "/start":
                    startCommandReceived(chatId, userName);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    log.info(userName + " displayed a list of commands");
                    break;
                default:
                    sendMessage(chatId, "Command not found");
                    log.info(userName + " entered a non-existent command");
            }
        }

    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Hi, " + name + ", nice to meet you!";
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSand) {
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

}
