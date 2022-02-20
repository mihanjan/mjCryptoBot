package ru.neoflex.mjcryptobot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotCredentials {
    @Value("${bot.name}")
    private String userName;
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.path}")
    private String webHookPath;
}
