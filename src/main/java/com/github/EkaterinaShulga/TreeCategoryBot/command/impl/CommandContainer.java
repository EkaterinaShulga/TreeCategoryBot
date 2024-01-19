package com.github.EkaterinaShulga.TreeCategoryBot.command.impl;

import com.github.EkaterinaShulga.TreeCategoryBot.command.Command;
import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands.*;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.CommandName.*;


@Slf4j
@Component
public class CommandContainer {


    /**
     * method creates map commands that the user can use
     * if we need to add a new command for the bot,
     * we create a command and add this command to this map
     *
     * @return Map - contains all commands expect the start command
     */

    @PostConstruct
    public Map<String, Command> createMapCommand() throws NullPointerException {
        Map<String, Command> mapCommand = new HashMap<>();
        mapCommand.put(VIEW_TREE.getMessage(), new ViewTreeCommand());
        mapCommand.put(EDD_ELEMENT.getMessage(), new AddElementCommand());
        mapCommand.put(ADD_SUB_ELEMENT.getMessage(), new AddSubElementCommand());
        mapCommand.put(REMOVE_ELEMENT.getMessage(), new RemoveElementCommand());
        mapCommand.put(DOWNLOAD.getMessage(), new DownloadCommand());
        mapCommand.put(HELP.getMessage(), new HelpCommand());
        return mapCommand;
    }
    /**
     * method accepts a user-entered command and calls the corresponding method
     * from the command container(map)
     *
     * @param text - user entered command
     * @param update - update from the bot
     * @param service - CategoryService
     */

    public void retrieveCommand(String text, Update update, CategoryService service) throws IOException {
        createMapCommand().get(text).execute(update, service);

    }

}
