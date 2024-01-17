package com.github.EkaterinaShulga.TreeCategoryBot.servise;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public interface TableService {
    InlineKeyboardMarkup startMenuButtons();

    List<InlineKeyboardButton> initButtons();
}
