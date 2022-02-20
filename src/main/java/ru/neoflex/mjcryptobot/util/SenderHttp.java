package ru.neoflex.mjcryptobot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.mjcryptobot.config.BotCredentials;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SenderHttp {

    private final BotCredentials botCredentials;
    private final RestTemplate restTemplate;

    public void sendTelegramMessage(long chatId, String msg) {
        String urlTemplate = "https://api.telegram.org/bot{token}/sendMessage?chat_id={chatId}&text={msg}";

        Map<String, String> params = new HashMap<>();
        params.put("token", botCredentials.getBotToken());
        params.put("chatId", String.valueOf(chatId));
        params.put("msg", msg);

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, String.class, params);
    }
}
