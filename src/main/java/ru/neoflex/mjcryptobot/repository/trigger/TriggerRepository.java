package ru.neoflex.mjcryptobot.repository.trigger;

public interface TriggerRepository {

    void add(String id, String trigger);

    String get(String id);

    Boolean delete(String id);
}
