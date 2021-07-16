package ru.home.botfortuna.botapi.handlers.menu;

import com.vdurmont.emoji.Emoji;
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
public class MainMenuHandler implements InputMessageHandler {

    private ReplyMessageService messageService;
    private MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessageService messageService, MainMenuService mainMenuService) {
        this.messageService = messageService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                messageService.getReplyText("reply.showMainMenu", Emojis.MAGE));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
