package ru.neoflex.mjcryptobot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.neoflex.mjcryptobot.bot.TelegramBot;
import ru.neoflex.mjcryptobot.bot.TelegramFacade;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BotCredentials botCredentials;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botCredentials.getWebHookPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot bot = new TelegramBot(setWebhook, telegramFacade);
        bot.setBotToken(botCredentials.getBotToken());
        bot.setBotUsername(botCredentials.getUserName());
        bot.setBotPath(botCredentials.getWebHookPath());
        return bot;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
