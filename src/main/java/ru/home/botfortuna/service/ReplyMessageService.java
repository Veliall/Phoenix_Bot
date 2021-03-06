package ru.home.botfortuna.service;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Сервис по отправке и принятии сообщений
 *
 * @author Igor Khristiuk
 */
@Service
@Component
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;

    @Autowired
    public ReplyMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getReplyMessage(String chatId, String replyMessage) {
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(String chatId, String replyMessage, Object... args) {
        return new SendMessage(chatId, localeMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String s, Object... args) {
        return localeMessageService.getMessage(s);
    }

    public String getReplyText(String s) {
        return localeMessageService.getMessage(s);
    }

}
