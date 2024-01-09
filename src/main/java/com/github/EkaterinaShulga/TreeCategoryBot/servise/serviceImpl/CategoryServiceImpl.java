package com.github.EkaterinaShulga.TreeCategoryBot.servise.serviceImpl;

import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import com.github.EkaterinaShulga.TreeCategoryBot.repository.CategoryRepository;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TelegramBot telegramBot;


    public static String INFORMATION_MESSAGE_CATEGORY_SAVED = " добавил категорию - ";
    public static String INFORMATION_MESSAGE_SUB_CATEGORY_SAVED = " добавил подкатегорию - ";
    public static String INFORMATION_MESSAGE_CATEGORY_IN_DATABASE = " такая категория уже есть в базе";
    public static String INFORMATION_MESSAGE_CATEGORY_NOT_FOUND = "   категория отсутствует в базе " +
            "или информация введена не корректно";
    public static String INFORMATION_MESSAGE_CATEGORY_DO_NOT_ADD = "  - такая категория отсутствует в базе  " +
            " или такая подкатегория уже есть ";
    public static String INFORMATION_MESSAGE_CATEGORY_DELETE = "  удалена категория и ее подкатегории  - ";
    public static String ATTENTION_MESSAGE = "Внимание! Информация введена не корректно";
    public static String INFORMATION_MESSAGE_ALL_FUNCTIONS_MENU =
            """
                     Вам доступны следующие команды:

                    /viewTree - отображает все имеющиеся категории и подкатегории;\s

                    /addElement <название элемента>  - добавляет категорию, если ее нет;

                    /addSubElement <название элемента>/<название подкатегории>\s
                    добавляет подкатегорию в категорию;\s

                    /removeElement <название категории> - удаляет категорию и все ее подкатегории;\s
                    
                    /help -  список всех доступных команд с их кратким описанием  """;


    /**
     * adds Category in the database
     * sends answer for user
     * (add category/this category is already in the database)
     *
     * @param update - update from the bot
     */
    @Override
    public void addCategory(Update update) {
        log.info("addCategory - CategoryServiceImpl");
        long chatId = update.message().chat().id();
        String text = update.message().text();
        String title = text.substring(12, text.length()).trim();
        Category category1 = categoryRepository.findByTitle(title);
        if (category1 == null) {
            Category category = new Category();
            category.setTitle(title);
            category.setParentId(0L);
            categoryRepository.save(category);
            telegramBot.execute(new SendMessage(chatId, INFORMATION_MESSAGE_CATEGORY_SAVED + title));
        } else {
            telegramBot.execute(new SendMessage(chatId, INFORMATION_MESSAGE_CATEGORY_IN_DATABASE));
        }
    }

    /**
     * adds a SubCategory in the database
     * sends answer for user
     * (add subCategory/this subCategory is already in the database/
     * not found parent category)
     *
     * @param update - update from the bot
     */
    @Override
    public void addCategoryCategory(Update update) {
        log.info("addCategoryCategory - CategoryServiceImpl");
        long chatId = update.message().chat().id();
        String text = update.message().text();
        int index = text.lastIndexOf("/");
        String titleCategoryParent = text.substring(15, index).trim();
        System.out.println(titleCategoryParent + " titleCategoryParent");
        String titleChild = text.substring(index + 1, text.length()).trim();
        System.out.println(titleChild + " titleChild");
        Category category1 = categoryRepository.findByTitle(titleCategoryParent);
        Category subCategory = categoryRepository.findByTitle(titleChild);
        if (category1 != null && subCategory == null) {
            Category category = new Category();
            category.setTitle(titleChild);
            category.setParentId(category1.getId());
            categoryRepository.save(category);

            telegramBot.execute(new SendMessage(chatId, INFORMATION_MESSAGE_SUB_CATEGORY_SAVED + titleChild +
                    " к  категории - " + titleCategoryParent));
            createCategoryTree(update);
        } else {
            telegramBot.execute(new SendMessage(chatId, titleCategoryParent +
                    INFORMATION_MESSAGE_CATEGORY_DO_NOT_ADD));
            createCategoryTree(update);
        }
    }

    /**
     * sends all information about categories and
     * subCategories from database
     * sends answer for user
     * (structured information/not found information)
     *
     * @param update - update from the bot
     */
    @Override
    public void createCategoryTree(Update update) {
        log.info("createCategoryTree - categoryServiceImpl");
        long chatId = update.message().chat().id();
        Category root = new Category(0L, "Категории:", 0L);
        List<String> list = new ArrayList<>();
        String f = "...";
        List<String> result = printHierarchyTree(root, 0, f, list);
        String res = createString(result);
        telegramBot.execute(new SendMessage(chatId, res));


    }

    /**
     * deletes Category (and all its subCategories)
     * from the database
     * sends answer for user
     * (delete Category/not found this category)
     *
     * @param update - update from the bot
     */
    @Override
    public void removeCategoryByTitle(Update update) {
        log.info("removeCategoryByTitle - categoryServiceImpl");
        long chatId = update.message().chat().id();
        String text = update.message().text();
        String title = text.substring(14, text.length()).trim();
        Category category = categoryRepository.findByTitle(title);
        if (category == null) {
            telegramBot.execute(new SendMessage(chatId, title + INFORMATION_MESSAGE_CATEGORY_NOT_FOUND));
        } else {
            List<Category> list = categoryRepository.findAllByParentId(category.getId());
            for (Category c : list) {
                categoryRepository.delete(c);
            }
            categoryRepository.delete(category);
            telegramBot.execute(new SendMessage(chatId, INFORMATION_MESSAGE_CATEGORY_DELETE + title));
        }

    }

    /**
     * method returns a list of commands and
     * information about all functions of bot
     *
     * @param update - update from the bot
     */
    @Override
    public void sendInformationAboutAllFunctions(Update update) {
        log.info("sendInformationAboutAllFunctions - categoryServiceImpl");
        try {
            String greetingMessage = INFORMATION_MESSAGE_ALL_FUNCTIONS_MENU;
            telegramBot.execute(new SendMessage(update.message().chat().id(), greetingMessage));
        } catch (NullPointerException e) {
            log.info(ATTENTION_MESSAGE + e);
        }


    }

    /**
     * method for structured output of information
     * to the user
     *
     * @return List
     */

    @Override
    public List<String> printHierarchyTree(Category root, int level, String f, List<String> list) {
        log.info("printHierarchyTree - categoryServiceImpl");
        for (int i = 0; i < level; i++)
            root.getTitle();
        list.add(f + root.getTitle());
        List<Category> subs = categoryRepository.findAllByParentId(root.getId());
        for (Category c : subs) {
            printHierarchyTree(c, level + 1, f + "..", list);
        }
        return list;
    }

    /**
     * create string  for sendMessage
     *
     * @return String
     */
    @Override
    public String createString(List<String> list) {
        log.info("createString - categoryServiceImpl");
        StringBuilder stb = new StringBuilder();
        for (String s : list) {
            stb.append("\t");
            stb.append("\n");
            stb.append(s);
        }
        return stb.toString();
    }


}








