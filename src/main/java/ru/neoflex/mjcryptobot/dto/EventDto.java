package ru.neoflex.mjcryptobot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {

    private String chatId;

    private String figi;

    public EventDto(TriggerDto triggerDto) {
        this.chatId = triggerDto.getChatId();
        this.figi = triggerDto.getFigi();
    }

    @SneakyThrows
    public static String toJsonString(EventDto eventDto) {
        return new ObjectMapper().writeValueAsString(eventDto);
    }

    @SneakyThrows
    public static EventDto from(String eventDto) {
        return new ObjectMapper().readValue(eventDto, EventDto.class);
    }
}
