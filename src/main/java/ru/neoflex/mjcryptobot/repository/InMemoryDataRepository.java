package ru.neoflex.mjcryptobot.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.neoflex.mjcryptobot.dto.CandleDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class InMemoryDataRepository {

    private final Map<Long, String> notifyChatIdFigiMap = new ConcurrentHashMap<>();
    private final Map<String, CandleDto> figiCandleMap = new ConcurrentHashMap<>();
}
