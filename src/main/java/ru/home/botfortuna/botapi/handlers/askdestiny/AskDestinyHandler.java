package ru.home.botfortuna.botapi.handlers.askdestiny;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.cache.UserDataCache;
import ru.home.botfortuna.service.LocaleMessageService;
import ru.home.botfortuna.service.ReplyMessageService;

/**
 * @author Igor Khristiuk
 */

@Slf4j
@Component
public class AskDestinyHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService replyMessageService;


    public AskDestinyHandler(UserDataCache userDataCache,
                             ReplyMessageService replyMessageService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }


    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput(org.telegram.telegrambots.meta.api.objects.Message inputMsg) {
        Long userId = inputMsg.getFrom().getId();
        Long chatId = inputMsg.getChatId();

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId.toString(), "reply.askDestiny");
        userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);

        return replyToUser;
    }
}
