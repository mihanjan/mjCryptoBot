package ru.neoflex.mjcryptobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MjCryptoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MjCryptoBotApplication.class, args);
    }
}
