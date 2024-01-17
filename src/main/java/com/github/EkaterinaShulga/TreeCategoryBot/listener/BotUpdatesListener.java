package com.github.EkaterinaShulga.TreeCategoryBot.listener;

import com.github.EkaterinaShulga.TreeCategoryBot.servise.impl.MessageHandler;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.ATTENTION;

@Slf4j
@RequiredArgsConstructor
@Service
public class BotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final MessageHandler messageHandler;


    @PostConstruct
    public void init() throws IOException {
        String json = Files.readString(Paths.get("update.json"));
        Update update = BotUtils.parseUpdate(json);
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                messageHandler.messageHandler(update);
            } catch (Exception e) {
                log.info(ATTENTION.getMessage() + e);
                telegramBot.execute(new SendMessage(update.message().chat().id(),
                        ATTENTION.getMessage()));
            }
        });
        log.info("returned an answer" + UpdatesListener.CONFIRMED_UPDATES_ALL);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}


