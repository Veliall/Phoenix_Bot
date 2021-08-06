package ru.home.botfortuna.botapi;

/**
 * Состояния бота
 *
 * @author Igor Khristiuk
 */
public enum BotState {
    ASK_NAME,
    ASK_BIRTHDAY,
    ASK_SCHOOL,
    ASK_ADDRESS,
    ASK_PHONE,
    ASK_PARENT_NAME,
    ASK_PARENT_BIRTHDAY,
    ASK_JOB,
    ASK_PARENT_PHONE,
    FILLING_PROFILE,
    PROFILE_FILLED,
    SHOW_MAIN_MENU,
    SHOW_CONTACTS,
    SHOW_USER_PROFILE;
}
