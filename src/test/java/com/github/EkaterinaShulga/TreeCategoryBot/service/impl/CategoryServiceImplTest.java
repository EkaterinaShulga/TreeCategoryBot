package com.github.EkaterinaShulga.TreeCategoryBot.service.impl;


import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import com.github.EkaterinaShulga.TreeCategoryBot.listener.BotUpdatesListener;
import com.github.EkaterinaShulga.TreeCategoryBot.repository.CategoryRepository;

import com.github.EkaterinaShulga.TreeCategoryBot.servise.impl.CategoryServiceImpl;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;

import com.pengrad.telegrambot.model.Update;


import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.TEXT_RESPONSE_CATEGORY_SAVED;
import static com.github.EkaterinaShulga.TreeCategoryBot.service.impl.TestUtils.getFileContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    private final Update responseAddingCategory = BotUtils.parseUpdate(getFileContent
            ("classpath:updates/update_3.json"));

    @Mock
    private CategoryRepository repository;

    @Mock
    private TelegramBot telegramBot;
    @InjectMocks
    private CategoryServiceImpl service;
    @Mock
    private BotUpdatesListener botUpdatesListener;
    private Category categoryParent;
    private Category categoryChild;


    @BeforeEach
    void init() {
        categoryParent = new Category();
        categoryParent.setId(1L);
        categoryParent.setTitle("платья");
        categoryParent.setParentId(0L);

        categoryChild = new Category();
        categoryChild.setId(2L);
        categoryChild.setTitle("макси");
        categoryChild.setParentId(1L);
    }


    @Test
    public void saveCategoryTestPositive() throws IOException {
        String info = replacedJson("/addElement платья");
        Update update = BotUtils.parseUpdate(info);

        when(repository.findByTitle(anyString())).thenReturn(null);
        service.addCategory(update);
        verify(repository, atMostOnce()).save(categoryParent);


    }

    @Test
    public void saveCategoryTestNegative() throws IOException {
        String info = replacedJson("/addElement платья");
        Update update = BotUtils.parseUpdate(info);
        when(repository.findByTitle(anyString())).thenReturn(categoryParent);
        service.addCategory(update);
        verify(repository, never()).save(categoryParent);

    }


    @Test
    public void removeCategoryByTitleTestPositive() throws IOException {
        String info = replacedJson("/removeElement платья");
        Update update = BotUtils.parseUpdate(info);
        when(repository.findByTitle(anyString())).thenReturn(categoryParent);
        service.removeCategoryByTitle(update);
        verify(repository, atMostOnce()).delete(categoryParent);

    }

    @Test
    public void removeCategoryByTitleTestNegative() throws IOException {
        String info = replacedJson("/removeElement платья");
        Update update = BotUtils.parseUpdate(info);
        when(repository.findByTitle(anyString())).thenReturn(null);
        service.removeCategoryByTitle(update);
        verify(repository, never()).delete(categoryParent);

    }

    @Test
    public void testUserSaveCategoryAnswerFromBot() throws IOException {
        botUpdatesListener.process(List.of(responseAddingCategory));
        String info = replacedJson("/addElement платья");
        Update update = BotUtils.parseUpdate(info);
        ArgumentCaptor<SendMessage> sentMessage = ArgumentCaptor.forClass(SendMessage.class);
        service.addCategory(update);
        verify(telegramBot).execute(sentMessage.capture());

        assertEquals(responseAddingCategory.message().chat().id(), sentMessage.getValue().getParameters().get("chat_id"));
        assertEquals(TEXT_RESPONSE_CATEGORY_SAVED.getMessage() +
                        categoryParent.getTitle()
                , sentMessage.getValue().getParameters().get("text"));
    }

    public String replacedJson(String replacement) throws IOException {
        String json = FileUtils.readFileToString(ResourceUtils.getFile("classpath:updates/update_3.json"),
                StandardCharsets.UTF_8);
        return json.replace("%data%", replacement);
    }

}
