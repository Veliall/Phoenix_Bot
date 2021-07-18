package ru.home.botfortuna.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.service.MainMenuService;
import ru.home.botfortuna.service.ReplyMessageService;

/**
 * @author Igor Khristiuk
 */

@Component
public class ShowContactsHandler implements InputMessageHandler {
    private final MainMenuService menuService;
    private final ReplyMessageService messageService;

    public ShowContactsHandler(MainMenuService menuService,
                               ReplyMessageService messageService) {
        this.menuService = menuService;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return menuService.getMainMenuMessage(message.getChatId(),
                messageService.getReplyText("reply.showPTA") + "\n" +
                messageService.getReplyText("reply.showPTA2") + "\n" +
                messageService.getReplyText("reply.showPTA3") + "\n" +
                messageService.getReplyText("reply.showDirector"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_CONTACTS;
    }
}
