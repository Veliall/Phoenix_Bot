package ru.home.botfortuna.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.service.MainMenuService;
import ru.home.botfortuna.service.ReplyMessageService;
import ru.home.botfortuna.utils.Emojis;

/**
 * @author Igor Khristiuk
 */

@Component
public class HelpMenuHandler implements InputMessageHandler {
    private MainMenuService menuService;
    private ReplyMessageService messageService;

    public HelpMenuHandler(MainMenuService menuService, ReplyMessageService messageService) {
        this.menuService = menuService;
        this.messageService = messageService;
    }


    @Override
    public SendMessage handle(Message message) {
        return menuService.getMainMenuMessage(message.getChatId(),
                messageService.getReplyText("reply.shoeHelpMenu", Emojis.MAGE));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
