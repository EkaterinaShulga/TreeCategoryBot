package com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands;

import com.github.EkaterinaShulga.TreeCategoryBot.command.Command;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class UploadCommand implements Command {
    public static String ANSWER_EXAMPLE_FOR_UPLOAD = """
            чтобы отправить Excel файл в базу данных и сохранить его там,
            вставьте и отправьте файл.\s
            \s
            ТРЕБОВАНИЯ К ФАЙЛУ!
            - бот принимает файлы с расширением xls\s
            - размер файла не должен превышать 20 МБ!\s
            - в файле не должны содержаться название таблицы и столбцов\s
            - нумерация id Категорий начинается с 1\s
            - перед добавление файла удалите ранее внесенные записи, 
            иначе информация может сохраниться не корректно\s
            """;

    /**
     * method processes commands coming from the user
     *
     * @param update  - update from the bot
     * @param service - CategoryService
     */
    @Override
    public void execute(Update update, CategoryService service) throws IOException {
        log.info("execute - uploadCommand");
        service.uploadExcelFile(update);
    }

    /**
     * method handles update.callbackQuery() coming from menu buttons
     * and sends a response to the user with an example of the command input
     *
     * @param bot    - TelegramBot
     * @param update - update from the bot
     */

    @Override
    public void executeForCallBackQuery(TelegramBot bot, Update update) {
        log.info("executeForCallBackQuery - uploadCommand");
        bot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                ANSWER_EXAMPLE_FOR_UPLOAD));


    }

}
