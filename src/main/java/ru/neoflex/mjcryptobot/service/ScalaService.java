package ru.neoflex.mjcryptobot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.neoflex.mjcryptobot.dto.EventDto;
import ru.neoflex.mjcryptobot.dto.TriggerDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ScalaService {

    private final Map<String, String> triggers = new ConcurrentHashMap<>();
    Random random = new Random();

    private final RedisTemplate<String, String> redisTemplate;

    public void setTrigger(String id, String trigger) {
        triggers.put(id, trigger);
    }

    @Scheduled(fixedRateString = "${schedule.trigger}")
    public void activateTrigger() {
        if (random.nextBoolean()) {
            List<String> keys = new ArrayList<>();

            triggers.forEach((key, value) -> {
                TriggerDto triggerDto = TriggerDto.from(value);
                // создаем event, который должен отправлятся в java-сервис:
                EventDto eventDto = new EventDto(triggerDto);
                // отправляем сообщение с событием в java-сервис
                redisTemplate.convertAndSend("mjChannel", EventDto.toJsonString(eventDto));
                keys.add(key);
            });

            keys.forEach(triggers::remove);
        }
    }
}
