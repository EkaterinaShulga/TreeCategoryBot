package com.github.EkaterinaShulga.TreeCategoryBot.command.impl;

import com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands.*;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.github.EkaterinaShulga.TreeCategoryBot.servise.serviceImpl.MessageHandler.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CallbackQueryContainer {

    private final ViewTreeCommand viewTree;
    private final AddElementCommand addElement;
    private final AddSubElementCommand addSubElement;
    private final RemoveElementCommand removeElement;
    private final HelpCommand help;

    public void answerForCallBackQuery(Update update) throws NullPointerException {
        log.info("answerForCallBackQuery - CallbackQueryContainer");
        String callBackQuery = update.callbackQuery().data();
        switch (callBackQuery) {
            case "просмотр всех категорий " -> viewTree.executeForCallBackQuery(update);
            case " добавить категорию " -> addElement.executeForCallBackQuery(update);
            case " добавить подкатегорию " -> addSubElement.executeForCallBackQuery(update);
            case " удалить категорию " -> removeElement.executeForCallBackQuery(update);
            case " помощь " -> help.executeForCallBackQuery(update);
            default -> throw new NullPointerException(ATTENTION_MESSAGE + callBackQuery);
        }
    }

}
