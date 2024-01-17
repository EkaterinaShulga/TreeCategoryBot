package com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands;

import com.github.EkaterinaShulga.TreeCategoryBot.command.Command;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ViewTreeCommand implements Command {

    public static String BUTTON_VIEW_TREE = """
            для просмотра всех категорий\s
            введите команду:
            /viewTree""";

    /**
     * method processes commands coming from the user
     *
     * @param update  - update from the bot
     * @param service - CategoryService
     */
    @Override
    public void execute(Update update, CategoryService service) {
        log.info("execute - ViewTreeCommand");
        service.createCategoryTree(update);

    }

    /**
     * method handles update.callbackQuery() coming from menu buttons
     * and sends a response to the user with an example of the command input
     *
     * @param bot    - TelegramBot
     * @param update - update from the bot
     */
    @Override
    public void executeForCallBackQuery(TelegramBot bot, Update update) {
        log.info("executeForCallBackQuery - ViewTreeCommand");
        bot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                BUTTON_VIEW_TREE));


    }
}
