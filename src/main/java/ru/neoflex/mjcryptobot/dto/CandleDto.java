package ru.neoflex.mjcryptobot.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.neoflex.mjcryptobot.util.CandleDeserializer;

@NoArgsConstructor
@Getter
@Setter
@JsonDeserialize(using = CandleDeserializer.class)
public class CandleDto {

    private String interval;

    private String figi;

    private String low;

    private String high;

    private String open;

    private String close;

    private String openTime;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("figi: " + figi).append(System.lineSeparator())
                .append("Интервал: " + interval).append(System.lineSeparator())
                .append("Нижняя граница: " + low).append(System.lineSeparator())
                .append("Верхняя граница: " + high).append(System.lineSeparator())
                .append("Стоимость на момент открытия: " + open).append(System.lineSeparator())
                .append("Стоимость на момент закрытия: " + close).append(System.lineSeparator())
                .append("Время открытия: " + openTime).toString();
    }
}