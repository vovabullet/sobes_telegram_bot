package com.example.sobes.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data // Автоматически создаёт конструкторы
@PropertySource("application.properties") //Указывает, где считывает свойства, которые считываются через value
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;
    @Value("${websocket.url}")
    String websocketUrl;
}
