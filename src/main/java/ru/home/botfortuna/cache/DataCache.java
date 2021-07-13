package ru.home.botfortuna.cache;

import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.handlers.fillingprofile.UserProfileData;

/**
 * @author Igor Khristiuk
 */
public interface DataCache {
    void setUsersCurrentBotState(Long userId, BotState botState);

    BotState getUsersCurrentBotState(Long userId);

    UserProfileData getUserProfileData(Long userId);

    void saveUserProfileData(Long userId, UserProfileData userProfileData);
}
