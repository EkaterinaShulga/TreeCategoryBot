package com.github.EkaterinaShulga.TreeCategoryBot.servise.impl;

import com.github.EkaterinaShulga.TreeCategoryBot.constants.BotButtonEnum;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.TableService;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    /**
     * method creates menu which consist names of functions bot
     *
     * @return InlineKeyboardMarkup - menu for users
     */
    @Override
    public InlineKeyboardMarkup startMenuButtons() {
        List<InlineKeyboardButton> buttons = initButtons();
        buttons.forEach(a -> a.callbackData(a.text()));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        buttons.forEach(markup::addRow);
        log.info("startMenuButtons - TableServiceImpl");
        return markup;
    }

    /**
     * method assigns names to buttons by accessing the enum
     *
     * @return List - with names buttons
     */
    @Override
    public List<InlineKeyboardButton> initButtons() {
        return Arrays.stream(BotButtonEnum.values()).
                map(a -> new InlineKeyboardButton(a.getMessage())).
                collect(Collectors.toList());
    }

}
