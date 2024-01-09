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
public class ViewTreeCommand implements Command {
    private final CategoryService categoryService;
    private final TelegramBot telegramBot;

    public static String BUTTON_VIEW_TREE = """
            для просмотра всех категорий\s
            введите команду:
            /viewTree""";

    @Override
    public void execute(Update update) {
        log.info("execute - ViewTreeCommand");
        categoryService.createCategoryTree(update);
    }

    @Override
    public void executeForCallBackQuery(Update update) {
        log.info("executeForCallBackQuery - ViewTreeCommand");
        telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                BUTTON_VIEW_TREE));


    }
}
