package com.github.EkaterinaShulga.TreeCategoryBot.servise.serviceImpl;

import com.github.EkaterinaShulga.TreeCategoryBot.servise.TableService;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.BotButtonEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    @Override
    public InlineKeyboardMarkup startMenuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        var buttonViewTree = new InlineKeyboardButton(BUTTON_COMMAND_VIEW_TREE.getMessage());
        var buttonAddCategory = new InlineKeyboardButton(BUTTON_COMMAND_ADD_CATEGORY.getMessage());
        var buttonAddSubCategory = new InlineKeyboardButton(BUTTON_COMMAND_ADD_SUBCATEGORY.getMessage());
        var buttonRemoveCategory = new InlineKeyboardButton(BUTTON_COMMAND_REMOVE_CATEGORY.getMessage());
        var buttonHelp = new InlineKeyboardButton(BUTTON_COMMAND_HELP.getMessage());

        buttonViewTree.callbackData(BUTTON_COMMAND_VIEW_TREE.getMessage());
        buttonAddCategory.callbackData(BUTTON_COMMAND_ADD_CATEGORY.getMessage());
        buttonAddSubCategory.callbackData(BUTTON_COMMAND_ADD_SUBCATEGORY.getMessage());
        buttonRemoveCategory.callbackData(BUTTON_COMMAND_REMOVE_CATEGORY.getMessage());
        buttonHelp.callbackData(BUTTON_COMMAND_HELP.getMessage());


        markup.addRow(buttonViewTree);
        markup.addRow(buttonAddCategory);
        markup.addRow(buttonAddSubCategory);
        markup.addRow(buttonRemoveCategory);
        markup.addRow(buttonHelp);
        log.info("startMenuButtons - TableServiceImpl");
        return markup;
    }

}
