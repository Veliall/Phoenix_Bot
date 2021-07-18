package ru.home.botfortuna.botapi.handlers.fillingprofile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.botfortuna.botapi.BotState;
import ru.home.botfortuna.botapi.InputMessageHandler;
import ru.home.botfortuna.cache.UserDataCache;
import ru.home.botfortuna.model.UserProfileData;
import ru.home.botfortuna.service.ReplyMessageService;
import ru.home.botfortuna.service.UsersProfileDataService;
import ru.home.botfortuna.utils.Emojis;


/**
 * Формирует анкету пользователя.
 */

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messagesService;
    private UsersProfileDataService usersProfileDataService;

    @Autowired
    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessageService messagesService,
                                 UsersProfileDataService usersProfileDataService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.usersProfileDataService = usersProfileDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        Long userId = inputMsg.getFrom().getId();
        String chatId = String.valueOf(inputMsg.getChatId());

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BIRTHDAY);
        }

        if (botState.equals(BotState.ASK_BIRTHDAY)) {
            profileData.setFullName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askBirthday");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SCHOOL);
        }

        if (botState.equals(BotState.ASK_SCHOOL)) {
            profileData.setBirthday(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSchool");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHONE);

        }

        if (botState.equals(BotState.ASK_PHONE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAddress");
            profileData.setSchool(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADDRESS);
        }

        if (botState.equals(BotState.ASK_ADDRESS)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askPhone");
            profileData.setPhone(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PARENT_NAME);
        }

        if (botState.equals(BotState.ASK_PARENT_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askParentName");
            profileData.setAddress(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PARENT_BIRTHDAY);
        }

        if (botState.equals(BotState.ASK_PARENT_BIRTHDAY)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askParentBirthday");
            profileData.setParentFullName(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_JOB);
        }

        if (botState.equals(BotState.ASK_JOB)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askJob", Emojis.SHRUGGING);
            profileData.setParentBirthday(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PARENT_PHONE);
        }

        if (botState.equals(BotState.ASK_PARENT_PHONE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askParentPhone");
            profileData.setJob(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setParentPhone(usersAnswer);

            profileData.setChatId(chatId);
            usersProfileDataService.saveUserProfileData(profileData);

            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            String profileFilledMessage = messagesService.getReplyText("reply.profileFilled",
                    profileData.getFullName());

            replyToUser = new SendMessage(chatId,
                    String.format("%s%n", profileFilledMessage));
            replyToUser.setParseMode("HTML");
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

}