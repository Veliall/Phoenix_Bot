package ru.home.botfortuna.botapi;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import ru.home.botfortuna.PhoenixTelegramBot;
import ru.home.botfortuna.cache.UserDataCache;
import ru.home.botfortuna.service.MainMenuService;
import ru.home.botfortuna.service.ReplyMessageService;

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
    private PhoenixTelegramBot phoenixTelegramBot;
    private ReplyMessageService replyMessageService;

    @Autowired
    public TelegramFacade(BotStateContext botStateContext,
                          UserDataCache userDataCache,
                          MainMenuService mainMenuService,
                          @Lazy PhoenixTelegramBot phoenixTelegramBot,
                          ReplyMessageService replyMessageService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.phoenixTelegramBot = phoenixTelegramBot;
        this.replyMessageService = replyMessageService;
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

        if (callbackQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(chatId.toString(), "Введите полное ФИО ребёнка");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BIRTHDAY);
        } else if (callbackQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Данная функция будет реализована позднее", false, callbackQuery);
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
                botState = BotState.SHOW_MAIN_MENU;
                phoenixTelegramBot.sendPhoto(chatId,replyMessageService.getReplyText("reply.hello"),
                        "static/images/144758_0.png");
                break;
            case "Список документов":
                phoenixTelegramBot.sendDocument(message.getChatId().toString(),
                        "Вы можете скачать документ с перечнем всего необходимого" +
                                " для зачисления ребёнка в ансамбль",
                        new File("Z:\\Users\\Феникс\\Desktop\\Программирование\\" +
                                "BotFortuna\\src\\main\\resources\\static\\docs\\" +
                                "Для зачисления ребёнка в образцовый театр танца.docx"));
                botState = BotState.SHOW_MAIN_MENU;
                break;
            case "Анкета":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Расписание":
                phoenixTelegramBot.sendPhoto(message.getChatId().toString(),
                        "Все изменения уточняйте у руководителя.",
                        "static/images/Скриншот 17-07-2021 20_44_20.jpg");
                botState = BotState.SHOW_MAIN_MENU;
                break;
            case "Контакты":
                botState = BotState.SHOW_CONTACTS;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

}
