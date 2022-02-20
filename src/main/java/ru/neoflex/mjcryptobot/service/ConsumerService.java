package ru.neoflex.mjcryptobot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.mjcryptobot.dto.CandleDto;
import ru.neoflex.mjcryptobot.repository.InMemoryDataRepository;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final InMemoryDataRepository inMemoryDataRepository;

    @KafkaListener(topics = "rht.candles", groupId = "hackathon-nullPointerException")
    public void consume(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        inMemoryDataRepository.getFigiCandleMap().put(consumerRecord.key(), new ObjectMapper().readValue(consumerRecord.value(), CandleDto.class));
    }
}
