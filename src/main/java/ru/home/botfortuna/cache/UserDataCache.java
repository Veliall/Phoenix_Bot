package ru.home.botfortuna.cache;

import org.springframework.stereotype.Component;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.model.UserProfileData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Khristiuk
 */
@Component
public class UserDataCache implements DataCache {
    private final Map<Long, BotState> usersBotStates = new HashMap<>();
    private final Map<Long, UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(Long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(Long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.SHOW_MAIN_MENU;
        }

        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(Long userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(Long userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }
}
