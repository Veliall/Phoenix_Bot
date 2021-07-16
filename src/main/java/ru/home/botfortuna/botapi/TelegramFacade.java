package ru.home.botfortuna.botapi;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import ru.home.botfortuna.FortunaTelegramBot;
import ru.home.botfortuna.model.UserProfileData;
import ru.home.botfortuna.cache.UserDataCache;
import ru.home.botfortuna.service.MainMenuService;
import ru.home.botfortuna.service.ReplyMessageService;

import java.io.*;
import java.io.File;

/**
 * @author Igor Khristiuk
 */
@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;
    private FortunaTelegramBot fortunaTelegramBot;
    private ReplyMessageService replyMessageService;

    @Autowired
    public TelegramFacade(BotStateContext botStateContext,
                          UserDataCache userDataCache,
                          MainMenuService mainMenuService,
                          @Lazy FortunaTelegramBot fortunaTelegramBot,
                          ReplyMessageService replyMessageService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.fortunaTelegramBot = fortunaTelegramBot;
        this.replyMessageService = replyMessageService;
    }

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final Long userId = callbackQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");

        //Destiny
        if (callbackQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(chatId.toString(), "Как тебя зовут?");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (callbackQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Возвращайся, когда будешь готов", false, callbackQuery);
        } else if (callbackQuery.getData().equals("buttonIWillThink")) {
            callBackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, callbackQuery);
        }

        //Gender
        else if (callbackQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("М");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = new SendMessage(chatId.toString(), "Твоя любимая цифра");
        } else if (callbackQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Ж");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = new SendMessage(chatId.toString(), "Твоя любимая цифра");

        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        return callBackAnswer;
    }

    private BotApiMethod<?> sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        String chatId = String.valueOf(message.getChatId());
        @NonNull Long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.ASK_DESTINY;
                fortunaTelegramBot.sendPhoto(chatId,replyMessageService.getReplyText("reply.hello"),
                        "static/images/507388.jpg");
                break;
            case "Получить предсказание":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Моя анкета":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Скачать анкету":
                fortunaTelegramBot.sendDocument(chatId, "Ваша анкета", getUserProfile(userId));
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private File getUserProfile(Long userId) {
        UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
        File profileFile = null;
        try {
            profileFile = ResourceUtils.getFile("classpath:static/docs/users_profile.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert profileFile != null;
            try (FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
                     BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(userProfileData.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profileFile;
    }
}
