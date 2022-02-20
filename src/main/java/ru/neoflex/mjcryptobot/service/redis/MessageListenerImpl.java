package ru.neoflex.mjcryptobot.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import ru.neoflex.mjcryptobot.dto.CandleDto;
import ru.neoflex.mjcryptobot.dto.EventDto;
import ru.neoflex.mjcryptobot.repository.InMemoryDataRepository;
import ru.neoflex.mjcryptobot.util.SenderHttp;

@Component
@RequiredArgsConstructor
public class MessageListenerImpl implements MessageListener {

    private final SenderHttp senderHttp;
    private final InMemoryDataRepository inMemoryDataRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventDto eventDto = EventDto.from(message.toString());
        CandleDto candleDto = inMemoryDataRepository.getFigiCandleMap().get(eventDto.getFigi());
        senderHttp.sendTelegramMessage(Long.parseLong(eventDto.getChatId()), "[Событие]" + System.lineSeparator() + candleDto.toString());
    }
}
