package ru.neoflex.mjcryptobot.repository.trigger;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TriggerRepositoryImpl implements TriggerRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void add(String id, String trigger) {
        redisTemplate.opsForValue().set(id, trigger);
    }

    @Override
    public String get(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    @Override
    public Boolean delete(String id) {
        return redisTemplate.delete(id);
    }
}
