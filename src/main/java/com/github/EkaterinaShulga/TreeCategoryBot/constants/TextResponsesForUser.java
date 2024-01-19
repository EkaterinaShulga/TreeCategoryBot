package com.github.EkaterinaShulga.TreeCategoryBot.constants;

public enum TextResponsesForUser {
    ATTENTION("Внимание! Информация введена не корректно"),
    TEXT_RESPONSE_CATEGORY_SAVED("добавил категорию -"),
    TEXT_RESPONSE_CATEGORY_IN_DATABASE("такая категория уже есть в базе -"),
    INFORMATION_MESSAGE_SUB_CATEGORY_SAVED("добавил подкатегорию -"),
    INFORMATION_MESSAGE_CATEGORY_NOT_FOUND("категория отсутствует в базе" +
            " или информация введена не корректно"),
    INFORMATION_MESSAGE_CATEGORY_DO_NOT_ADD("- такая категория отсутствует в базе" +
            " или такая подкатегория уже есть"),
    INFORMATION_MESSAGE_CATEGORY_DELETE("удалена категория и ее подкатегории  -"),

    INFORMATION_MESSAGE_DATABASE_IS_IMPTY("база данных пуста, чтобы получить exel документ," +
            " добавьте хотя бы одну категорию "),
    INFORMATION_MESSAGE_CREATE_EXCEL_FILE("по вашему запросу создал exel документ"),

    INFORMATION_MESSAGE_ALL_FUNCTIONS_MENU
            ("""
                    Вам доступны следующие команды:

                    /viewTree - отображает все имеющиеся категории и подкатегории;\s

                    /addElement <название элемента>  - добавляет категорию, если ее нет;

                    /addSubElement <название элемента>/<название подкатегории>\s
                    добавляет подкатегорию в категорию;\s

                    /removeElement <название категории> - удаляет категорию и все ее подкатегории;\s
                                        
                    /download  - создает Excel документ, с деревом категорий(из базы данных);\s
                                        
                    /help -  список всех доступных команд с их кратким описанием""");


    private final String message;

    TextResponsesForUser(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
