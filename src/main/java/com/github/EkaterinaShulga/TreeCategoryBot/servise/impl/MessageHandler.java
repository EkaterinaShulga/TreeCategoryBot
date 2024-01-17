package com.github.EkaterinaShulga.TreeCategoryBot.servise.impl;

import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.CallbackQueryContainer;
import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.CommandContainer;
import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands.StartCommand;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;


import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.ATTENTION;


@Slf4j
@RequiredArgsConstructor
@Service
public class MessageHandler {

    private final TelegramBot telegramBot;
    private final CommandContainer commandContainer;
    private final CategoryService categoryService;
    private final CallbackQueryContainer callbackQueryContainer;
    private final StartCommand startCommand;

    /**
     * checks callbackQuery which came from user menu buttons
     * and checks commands which came from user
     *
     * @param update - update from the bot
     */
    public void messageHandler(Update update) throws IOException {
        log.info("messageHandler - MessageHandler");

        if (update.callbackQuery() != null) {
            callbackQueryContainer.retrieveCallbackQuery(update);
        } else if (update.message().text() != null && update.message().text().trim().startsWith("/")) {
            String text = update.message().text();
            if (text.equals("/start")) {
                startCommand.executeForCallBackQuery(telegramBot, update);
            } else {
                commandContainer.retrieveCommand(text.trim().split(" ")[0], update, categoryService);
            }
        } else {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ATTENTION.getMessage()));
        }
    }


}


