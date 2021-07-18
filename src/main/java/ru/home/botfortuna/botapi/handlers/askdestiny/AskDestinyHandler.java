package ru.home.botfortuna.botapi.handlers.askdestiny;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.service.ReplyMessageService;

/**
 * @author Igor Khristiuk
 */

@Slf4j
@Component
public class AskDestinyHandler implements InputMessageHandler {
    private ReplyMessageService replyMessageService;


    public AskDestinyHandler(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }


    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }

    private SendMessage processUsersInput(org.telegram.telegrambots.meta.api.objects.Message inputMsg) {
        Long chatId = inputMsg.getChatId();

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId.toString(), "reply.askDestiny");

        return replyToUser;
    }
}
