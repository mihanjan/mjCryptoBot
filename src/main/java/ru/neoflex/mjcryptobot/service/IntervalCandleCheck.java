package ru.neoflex.mjcryptobot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.neoflex.mjcryptobot.repository.InMemoryDataRepository;
import ru.neoflex.mjcryptobot.util.SenderHttp;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IntervalCandleCheck {

    private final InMemoryDataRepository inMemoryDataRepository;
    private final SenderHttp senderHttp;

    @Scheduled(fixedRateString = "${schedule.candle}")
    public void checkCoin() {
        final Map<Long, String> chatIdCandleMap = new HashMap<>();
        inMemoryDataRepository.getNotifyChatIdFigiMap().forEach(
                (chatId, figi) -> chatIdCandleMap.put(chatId, inMemoryDataRepository.getFigiCandleMap().get(figi).toString()));

        chatIdCandleMap.forEach(senderHttp::sendTelegramMessage);
    }
}
