package com.github.EkaterinaShulga.TreeCategoryBot.command.impl;

import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands.*;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Slf4j
@RequiredArgsConstructor
@Component
public class CommandContainer {
    private  final ViewTreeCommand viewTree;
    private  final AddElementCommand addElement;
    private  final AddSubElementCommand addSubElement;
    private  final RemoveElementCommand removeElement;
    private  final HelpCommand help;


    public void retrieveCommand(String command, Update update) {
        log.info("retrieveCommand - CommandContainer");
        System.out.println(command);
        switch (command) {
            case "/viewTree" -> viewTree.execute(update);
            case "/addElement" -> addElement.execute(update);
            case "/addSubElement" -> addSubElement.execute(update);
            case "/removeElement" -> removeElement.execute(update);
            case "/help" -> help.execute(update);

        }

    }


}
