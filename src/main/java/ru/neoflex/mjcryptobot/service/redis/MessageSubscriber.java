package ru.neoflex.mjcryptobot.service.redis;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSubscriber {

    public MessageSubscriber(RedisTemplate redisTemplate, MessageListenerImpl messageListenerImpl) {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        redisConnection.subscribe(messageListenerImpl, "mjChannel".getBytes());
    }
}

