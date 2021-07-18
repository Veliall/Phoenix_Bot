package ru.home.botfortuna.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Обрабтчик сообщений
 *
 * @author Igor Khristiuk
 */
public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
