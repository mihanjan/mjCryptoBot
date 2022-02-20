package ru.neoflex.mjcryptobot.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.neoflex.mjcryptobot.dto.CandleDto;

import java.io.IOException;

public class CandleDeserializer extends JsonDeserializer<CandleDto> {

    @Override
    public CandleDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        CandleDto candleDto = new CandleDto();

        JsonNode candleNode = node.get("candle");
        candleDto.setInterval(candleNode.get("interval").asText());
        candleDto.setFigi(candleNode.get("figi").asText());

        JsonNode candleDetailsNode = candleNode.get("details");
        candleDto.setLow(candleDetailsNode.get("low").asText());
        candleDto.setHigh(candleDetailsNode.get("high").asText());
        candleDto.setOpen(candleDetailsNode.get("open").asText());
        candleDto.setClose(candleDetailsNode.get("close").asText());
        candleDto.setOpenTime(candleDetailsNode.get("openTime").asText());

        return candleDto;
    }
}
