package com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands;

import com.github.EkaterinaShulga.TreeCategoryBot.command.Command;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AddElementCommand implements Command {

    private final CategoryService categoryService;
    private final TelegramBot telegramBot;

    public static String BUTTON_EXAMPLE_FOR_NEW_CATEGORY = """
             чтобы добавить категорию
            введите команду и укажите категорию:\s
            /addElement верхняя одежда\s""";

    @Override
    public void execute(Update update) {
        log.info("execute - addElementCommand");
        categoryService.addCategory(update);
    }

    @Override
    public void executeForCallBackQuery(Update update) {
        log.info("executeForCallBackQuery - addElementCommand");
        telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                BUTTON_EXAMPLE_FOR_NEW_CATEGORY));


    }

}
