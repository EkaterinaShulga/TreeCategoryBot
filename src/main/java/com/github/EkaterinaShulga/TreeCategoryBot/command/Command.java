package com.github.EkaterinaShulga.TreeCategoryBot.command;


import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import java.io.IOException;

public interface Command {
    void execute(Update update, CategoryService service) throws IOException;

    void executeForCallBackQuery(TelegramBot bot, Update update);
}
