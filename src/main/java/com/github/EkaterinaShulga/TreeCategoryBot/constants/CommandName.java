package com.github.EkaterinaShulga.TreeCategoryBot.constants;

public enum CommandName {


    START("/start"),
    VIEW_TREE("/viewTree"),
    EDD_ELEMENT("/addElement"),
    ADD_SUB_ELEMENT("/addSubElement"),
    REMOVE_ELEMENT("/removeElement"),
    DOWNLOAD("/download"),
    UPLOAD("/upload"),
    HELP("/help");

    private final String message;

    CommandName(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
