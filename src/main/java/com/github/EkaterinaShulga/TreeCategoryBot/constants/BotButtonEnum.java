package com.github.EkaterinaShulga.TreeCategoryBot.constants;

public enum BotButtonEnum {
    /**
     * contains with text
     * (for the buttons of menu and
     * text for answers/information for users )
     */

    BUTTON_COMMAND_VIEW_TREE("просмотр всех категорий "),
    BUTTON_COMMAND_ADD_CATEGORY(" добавить категорию "),
    BUTTON_COMMAND_ADD_SUBCATEGORY(" добавить подкатегорию "),
    BUTTON_COMMAND_REMOVE_CATEGORY(" удалить категорию "),
    BUTTON_COMMAND_HELP(" помощь ");

    private final String message;

    BotButtonEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
