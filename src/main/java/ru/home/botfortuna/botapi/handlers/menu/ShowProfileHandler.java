package ru.home.botfortuna.botapi.handlers.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.model.UserProfileData;
import ru.home.botfortuna.cache.UserDataCache;
import ru.home.botfortuna.service.UsersProfileDataService;

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
            userReply = new SendMessage(message.getChatId().toString(), "Такой анкеты в БД не существует !");
        }

        return userReply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
