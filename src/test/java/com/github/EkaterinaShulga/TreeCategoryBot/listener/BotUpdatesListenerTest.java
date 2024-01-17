package com.github.EkaterinaShulga.TreeCategoryBot.listener;

import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import com.github.EkaterinaShulga.TreeCategoryBot.repository.CategoryRepository;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.TEXT_RESPONSE_CATEGORY_SAVED;
import static com.github.EkaterinaShulga.TreeCategoryBot.service.impl.TestUtils.getFileContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class BotUpdatesListenerTest {

    @MockBean
    private TelegramBot telegramBot;

    private final Update helloUpdate = BotUtils.parseUpdate(getFileContent("classpath:updates/update_1.json"));
    private final Update responseAddingCategory = BotUtils.parseUpdate(getFileContent("classpath:updates/update_4.json"));

    @Autowired
    private BotUpdatesListener botUpdatesListener;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category categoryParent;


    @BeforeEach
    void init() {
        categoryParent = new Category();
        categoryParent.setId(1L);
        categoryParent.setTitle("зонты");
        categoryParent.setParentId(0L);

    }

    private String greetingMessage = String.format("Hello! @%s  выберите нужную команду.",
            helloUpdate.message().chat().username());

    @Test
    public void testHelloMessage() {
        botUpdatesListener.process(List.of(helloUpdate));

        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sentMessage.capture());
        assertEquals(helloUpdate.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(greetingMessage, sentMessage.getValue().getParameters().get("text"));
    }

    @Test
    public void testUserSaveCategory() {
        botUpdatesListener.process(List.of(responseAddingCategory));

        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sentMessage.capture());

        assertEquals(responseAddingCategory.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(TEXT_RESPONSE_CATEGORY_SAVED.getMessage() + categoryParent.getTitle(), sentMessage.getValue().getParameters().get("text"));


    }


}
