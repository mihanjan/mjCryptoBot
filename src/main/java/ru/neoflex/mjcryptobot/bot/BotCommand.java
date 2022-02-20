package ru.neoflex.mjcryptobot.bot;

import java.util.Arrays;

public enum BotCommand {
    HOME("/home"),
    RUN_STREAM("/runstream"),
    CANCEL("/cancel"),
    CHANGE_FROM_LEVEL("/changefromlevel"),
    REACHING_LEVEL("/reachinglevel"),
    NONE("");

    private String cmd;

    BotCommand(String cmd) {
        this.cmd = cmd;
    }

    public static BotCommand from(String cmd) {
        return Arrays.stream(BotCommand.values())
                .filter(botCommand -> botCommand.cmd.equals(cmd))
                .findFirst()
                .orElse(NONE);
    }
}
