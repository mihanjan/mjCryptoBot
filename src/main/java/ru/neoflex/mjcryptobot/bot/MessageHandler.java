package ru.neoflex.mjcryptobot.bot;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neoflex.mjcryptobot.dto.TriggerDto;
import ru.neoflex.mjcryptobot.repository.InMemoryDataRepository;
import ru.neoflex.mjcryptobot.repository.trigger.TriggerRepository;
import ru.neoflex.mjcryptobot.service.ScalaService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final InMemoryDataRepository inMemoryDataRepository;
    private final TriggerRepository triggerRepository;

    private final ScalaService scalaService;

    private final Map<Long, TriggerDto> cacheTriggers = new ConcurrentHashMap<>();

    public BotApiMethod<?> handle(Message message) {
        final Long chatId = message.getChatId();
        final String inputText = message.getText();
        BotCommand botCommand = BotCommand.from(inputText);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        switch (botCommand) {
            case HOME -> {
                ChatState.setChatState(chatId, ChatState.NONE);
                cacheTriggers.remove(chatId);
                sendMessage.setText("Бот возвращён в начальное состояние");
            }

            case RUN_STREAM -> {
                ChatState.setChatState(chatId, ChatState.WAIT_FOR_FIGI);
                sendMessage.setText("Введите figi криптовалюты, которую хотите отслеживать");
            }

            case CANCEL -> {
                ChatState.setChatState(chatId, ChatState.NONE);
                if (inMemoryDataRepository.getNotifyChatIdFigiMap().containsKey(chatId)) {
                    inMemoryDataRepository.getNotifyChatIdFigiMap().remove(chatId);
                    sendMessage.setText("Отслеживание криптовалюты отключено");
                } else {
                    sendMessage.setText("Сейчас не отслеживается ни одной криптовалюты");
                }
            }

            case CHANGE_FROM_LEVEL -> {
                ChatState.setChatState(chatId, ChatState.WAIT_FOR_FIGI_CHANGE);
                cacheTriggers.put(chatId, new TriggerDto(String.valueOf(chatId)));
                sendMessage.setText("Вы ввели команду для активации подписки на события вида 'изменение курса валюты C от текущего на X%'\n" +
                        "Введите figi валюты");
            }

            case REACHING_LEVEL -> {
                ChatState.setChatState(chatId, ChatState.WAIT_FOR_FIGI_REACH);
                cacheTriggers.put(chatId, new TriggerDto(String.valueOf(chatId)));
                sendMessage.setText("Вы ввели команду для активации подписки на события вида 'достижение курса валюты C стоимости Х'\n" +
                        "Введите figi валюты");
            }

            default -> sendMessage.setText(handleAnotherText(chatId, inputText));
        }
        return sendMessage;
    }

    private String handleAnotherText(long chatId, String inputText) {
        ChatState chatState = ChatState.getChatState(chatId);

        switch (chatState) {
            case WAIT_FOR_FIGI -> {
                if (containsFigi(inputText)) {
                    inMemoryDataRepository.getNotifyChatIdFigiMap().put(chatId, inputText);
                    ChatState.setChatState(chatId, ChatState.NONE);
                    return "Теперь вы отслеживаете " + inputText;
                }
                return "Введен figi, отстутствующий в базе данных системы, попробуйте ввести еще раз";
            }

            case WAIT_FOR_FIGI_CHANGE -> {
                if (containsFigi(inputText)) {
                    cacheTriggers.get(chatId).setFigi(inputText);
                    ChatState.setChatState(chatId, ChatState.WAIT_FOR_PERCENT);
                    return "Введите, на сколько процентов должна изменится валюта от текущего значения.\n" +
                            "Введите отрицательное число, если валюта должна упасть (например: -17)";
                }
                return "Введен figi, отстутствующий в базе данных системы, попробуйте ввести еще раз";
            }

            case WAIT_FOR_PERCENT -> {
                if (Longs.tryParse(inputText) != null) {
                    TriggerDto triggerDto = cacheTriggers.get(chatId);
                    triggerDto.setPercent(Ints.tryParse(inputText));
                    sendTrigger(chatId, triggerDto);
                    return "Подписка оформлена";
                }
                return "Введенное значение не соответствует формату, попробуйте еще раз";
            }

            case WAIT_FOR_FIGI_REACH -> {
                if (containsFigi(inputText)) {
                    cacheTriggers.get(chatId).setFigi(inputText);
                    ChatState.setChatState(chatId, ChatState.WAIT_FOR_PRICE);
                    return "Введите цену, по достижению которой придет уведомление";
                }
                return "Введен figi, отстутствующий в базе данных системы, попробуйте ввести еще раз";
            }

            case WAIT_FOR_PRICE -> {
                if (Doubles.tryParse(inputText) != null) {
                    TriggerDto triggerDto = cacheTriggers.get(chatId);
                    triggerDto.setPrice(Doubles.tryParse(inputText));
                    sendTrigger(chatId, triggerDto);
                    return "Подписка оформлена";
                }
                return "Введенное значение не соответствует формату, попробуйте еще раз";
            }

            default -> {
                return "Введите команду из списка";
            }
        }
    }

    private boolean containsFigi(String figi) {
        return List.of("BNB", "DOT", "DOGE", "ADA", "BTC", "ETH").contains(figi);
    }

    private void sendTrigger(long chatId, TriggerDto triggerDto) {
        String triggerId = String.valueOf(triggerDto.getId());
        triggerRepository.add(triggerId, TriggerDto.toJsonString(triggerDto));

        // имитация того, что scala-сервис забрал триггер:
        scalaService.setTrigger(triggerId, triggerRepository.get(triggerId));
        triggerRepository.delete(triggerId);

        cacheTriggers.remove(chatId);
        ChatState.setChatState(chatId, ChatState.NONE);
    }
}
