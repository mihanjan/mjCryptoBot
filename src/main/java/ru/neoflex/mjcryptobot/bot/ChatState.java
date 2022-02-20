package ru.neoflex.mjcryptobot.bot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ChatState {
    WAIT_FOR_FIGI,
    WAIT_FOR_FIGI_CHANGE,
    WAIT_FOR_PERCENT,
    WAIT_FOR_FIGI_REACH,
    WAIT_FOR_PRICE,
    NONE;

    private static final Map<Long, ChatState> map = new ConcurrentHashMap<>();

    public static ChatState getChatState(long chatId) {
        return map.getOrDefault(chatId, NONE);
    }

    public static void setChatState(long chatId, ChatState chatState) {
        map.put(chatId, chatState);
    }
}
