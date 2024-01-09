package com.github.EkaterinaShulga.TreeCategoryBot.command;


import com.pengrad.telegrambot.model.Update;

public interface Command {
    void execute(Update update);

    void executeForCallBackQuery(Update update);
}
