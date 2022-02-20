package ru.neoflex.mjcryptobot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@NoArgsConstructor
public class TriggerDto {

    private static AtomicLong counter = new AtomicLong(0);

    private Long id;

    private String chatId;

    private String figi;

    private Integer percent;

    private Double price;

    public TriggerDto(String chatId) {
        id = counter.incrementAndGet();
        this.chatId = chatId;
    }

    @SneakyThrows
    public static TriggerDto from(String trigger) {
        return new ObjectMapper().readValue(trigger, TriggerDto.class);
    }

    @SneakyThrows
    public static String toJsonString(TriggerDto triggerDto) {
        return new ObjectMapper().writeValueAsString(triggerDto);
    }
}
