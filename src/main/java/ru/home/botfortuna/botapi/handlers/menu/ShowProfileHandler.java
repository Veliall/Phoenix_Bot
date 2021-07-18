package ru.home.botfortuna.botapi.handlers.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.model.UserProfileData;
import ru.home.botfortuna.cache.UserDataCache;
import ru.home.botfortuna.service.UsersProfileDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Khristiuk
 */
@Component
public class ShowProfileHandler implements InputMessageHandler {

    private UserDataCache userDataCache;
    private UsersProfileDataService profileDataService;

    @Autowired
    public ShowProfileHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.profileDataService = profileDataService;
    }

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final Long userId = message.getFrom().getId();
        final UserProfileData profileData = profileDataService.getUserProfileData(String.valueOf(message.getChatId()));

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        SendMessage userReply;
        if (profileData != null) {
            userReply = new SendMessage(message.getChatId().toString(),
                    String.format("%s%n-------------------%n%s", "Данные по вашей анкете:", profileData));
        } else {
            userReply = new SendMessage(message.getChatId().toString(), "Вашей анкеты в базе данных не существует." +
                    "Пожалуйста, создайте и заполните.");
        }
        userReply.setReplyMarkup(getInlineMessageButtons());

        return userReply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText("Создать новую анкету");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText("Внести изменение");

        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
