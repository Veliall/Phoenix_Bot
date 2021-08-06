package ru.home.botfortuna.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Обработчик сообщений для каждого состояния
 *
 * @author Igor Khristiuk
 */
@Component
public class BotStateContext {
    private final Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.FILLING_PROFILE);
        }

        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_BIRTHDAY:
            case ASK_SCHOOL:
            case ASK_ADDRESS:
            case ASK_PHONE:
            case ASK_PARENT_NAME:
            case ASK_PARENT_BIRTHDAY:
            case ASK_JOB:
            case ASK_PARENT_PHONE:
            case FILLING_PROFILE:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }


}
