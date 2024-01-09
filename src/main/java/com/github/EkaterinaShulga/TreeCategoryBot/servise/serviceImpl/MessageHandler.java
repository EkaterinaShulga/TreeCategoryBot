package com.github.EkaterinaShulga.TreeCategoryBot.servise.serviceImpl;

import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.CallbackQueryContainer;
import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.CommandContainer;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.TableService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@Service
public class MessageHandler {

    private final TelegramBot telegramBot;
    private final TableService tableService;
    private final CommandContainer commandContainer;
    private final CallbackQueryContainer callbackQueryContainer;
    public static String COMMAND_PREFIX = "/";

    public static String HELLO_MESSAGE = " Hello! @";
    public static String BUTTON_START_MESSAGE = " выберите нужную команду ";
    public static String ATTENTION_MESSAGE = "Внимание! Информация введена не корректно";
    public static String START = "/start";

    /**
     * checks callbackQuery which came from user menu buttons
     *
     * @param update - update from the bot
     */
    public void messageHandler(Update update) throws IOException {
        log.info("messageHandler - MessageHandler");
        if (update.callbackQuery() != null) {
            callbackQueryContainer.answerForCallBackQuery(update);
        } else {
            checkAnswer(update);
        }
    }

    /**
     * checks input messagies/commands from user
     *
     * @param update - update from the bot
     */
    public void checkAnswer(Update update) throws IOException {
        log.info("checkAnswer - MessageHandler");
        if (update.message().text() != null && update.message().text().equals(START)) {
            sendStartMenu(update);
        }
        if (!update.message().text().equals(START) && update.message().text().startsWith(COMMAND_PREFIX)) {
            String message = update.message().text().trim();
            String commandIdentifier = message.split(" ")[0];
            commandContainer.retrieveCommand(commandIdentifier, update);
        }
        if (!update.message().text().startsWith(COMMAND_PREFIX)) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ATTENTION_MESSAGE));
        }
    }


    /**
     * method returns a welcome message and
     * menu to the user
     *
     * @param update - update from the bot
     */
    public void sendStartMenu(Update update) {
        log.info("sendStartMenu - MessageHandler");
        try {
            String greetingMessage = HELLO_MESSAGE + update.message().chat().username()
                    + BUTTON_START_MESSAGE;
            telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                    .replyMarkup(tableService.startMenuButtons()));
        } catch (NullPointerException e) {
            log.info(ATTENTION_MESSAGE + e);
        }
    }


}


