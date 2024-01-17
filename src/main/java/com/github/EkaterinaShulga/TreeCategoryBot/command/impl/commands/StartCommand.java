package com.github.EkaterinaShulga.TreeCategoryBot.command.impl.commands;

import com.github.EkaterinaShulga.TreeCategoryBot.command.Command;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.TableService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.ATTENTION;


@Slf4j
@RequiredArgsConstructor
@Component
public class StartCommand implements Command {

    private final TableService tableService;

    @Override
    public void execute(Update update, CategoryService service) {
    }

    /**
     * method returns a welcome message and
     * menu to the user
     *
     * @param bot    - TelegramBot
     * @param update - update from the bot
     */
    @Override
    public void executeForCallBackQuery(TelegramBot bot, Update update) {
        log.info("executeForCallBackQuery - startCommand");
        try {
            String greetingMessage = String.format("Hello! @%s  выберите нужную команду.",
                    update.message().chat().username());
            bot.execute(new SendMessage(update.message().chat().id(), greetingMessage)
                    .replyMarkup(tableService.startMenuButtons()));
        } catch (NullPointerException e) {
            log.info(ATTENTION.getMessage() + e);
        }
    }


}

