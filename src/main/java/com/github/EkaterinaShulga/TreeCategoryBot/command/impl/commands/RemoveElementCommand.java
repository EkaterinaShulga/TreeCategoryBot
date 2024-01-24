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
public class RemoveElementCommand implements Command {

    public static String ANSWER_EXAMPLE_FOR_REMOVE_CATEGORY = """
             чтобы удалить категорию
            введите команду и название категории или подкатегории:\s
            /removeElement верхняя одежда\s""";

    /**
     * method processes commands coming from the user
     *
     * @param update  - update from the bot
     * @param service - CategoryService
     */

    @Override
    public void execute(Update update, CategoryService service) {
        log.info("execute - removeElementCommand");
        service.removeCategoryByTitle(update);
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
        log.info("executeForCallBackQuery - removeElementCommand");
        bot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                ANSWER_EXAMPLE_FOR_REMOVE_CATEGORY));
    }

}
