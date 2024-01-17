package com.github.EkaterinaShulga.TreeCategoryBot.command.impl;

import com.github.EkaterinaShulga.TreeCategoryBot.command.Command;
import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.BotButtonEnum.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CallbackQueryContainer {

    private final TelegramBot bot;

    /**
     * method creates a map of all commands coming from menu buttons
     *
     * @return Map - contains all commands expect the start command
     */
    @PostConstruct
    public Map<String, Command> createMapCallBackQuery() throws NullPointerException {
        Map<String, Command> mapCallBackQuery = new HashMap<>();
        mapCallBackQuery.put(BUTTON_COMMAND_VIEW_TREE.getMessage(), new ViewTreeCommand());
        mapCallBackQuery.put(BUTTON_COMMAND_ADD_CATEGORY.getMessage(), new AddElementCommand());
        mapCallBackQuery.put(BUTTON_COMMAND_ADD_SUBCATEGORY.getMessage(), new AddSubElementCommand());
        mapCallBackQuery.put(BUTTON_COMMAND_REMOVE_CATEGORY.getMessage(), new RemoveElementCommand());
        mapCallBackQuery.put(BUTTON_COMMAND_REMOVE_CATEGORY.getMessage(), new RemoveElementCommand());
        mapCallBackQuery.put(BUTTON_COMMAND_HELP.getMessage(), new HelpCommand());
        return mapCallBackQuery;
    }

    /**
     * method accepts the response from the menu button,
     * looks for the command in the map and calls the method
     * to send the appropriate message to the user
     * (how to correctly enter the selected command)
     *
     * @param update - update from the bot
     */
    public void retrieveCallbackQuery(Update update) {
        String text = update.callbackQuery().data();
       createMapCallBackQuery().get(text)
                .executeForCallBackQuery(bot, update);
    }

}
