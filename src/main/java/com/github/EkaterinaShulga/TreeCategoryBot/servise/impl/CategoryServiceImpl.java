package com.github.EkaterinaShulga.TreeCategoryBot.servise.impl;

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

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TelegramBot telegramBot;

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
        int index = text.indexOf(" ");
        String title = text.substring(index).trim();
        Category categoryFromBase = categoryRepository.findByTitle(title);
        log.info("Category was found: {}", categoryFromBase);
        if (categoryFromBase == null) {
            Category category = new Category();
            category.setTitle(title);
            category.setParentId(0L);
            categoryRepository.save(category);
            log.info("Category was added: {}", category);
            telegramBot.execute(new SendMessage(chatId,
                    TEXT_RESPONSE_CATEGORY_SAVED.getMessage() + title));
        } else {
            telegramBot.execute(new SendMessage(chatId, TEXT_RESPONSE_CATEGORY_IN_DATABASE.getMessage()));
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
        int indexFirst = text.indexOf(" ");
        int index = text.lastIndexOf("/");

        String titleCategoryParent = text.substring(indexFirst, index).trim();
        String titleChild = text.substring(index + 1).trim();
        Category categoryParentFromBase = categoryRepository.findByTitle(titleCategoryParent);
        log.info("Category was found: {}", categoryParentFromBase);
        Category subCategory = categoryRepository.findByTitle(titleChild);
        log.info("Category was found: {}", subCategory);
        if (categoryParentFromBase != null && subCategory == null) {
            Category category = new Category();
            category.setTitle(titleChild);
            category.setParentId(categoryParentFromBase.getId());
            categoryRepository.save(category);
            log.info("SubCategory was added: {}", category);
            telegramBot.execute(new SendMessage(chatId,
                    INFORMATION_MESSAGE_SUB_CATEGORY_SAVED.getMessage() + titleChild +
                            "к  категории - " + titleCategoryParent));

            createCategoryTree(update);
        } else {
            telegramBot.execute(new SendMessage(chatId, titleCategoryParent + " " +
                    INFORMATION_MESSAGE_CATEGORY_DO_NOT_ADD.getMessage()));

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
        String f = "..";
        String m = "..";
        List<Category> allCategories = categoryRepository.findAll();
        List<String> result = createHierarchicalStructure(root, f, m, list, allCategories);
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
        int index = text.indexOf(" ");
        String title = text.substring(index).trim();
        Category category = categoryRepository.findByTitle(title);
        log.info("Category was found: {}", category);
        if (category == null) {
            telegramBot.execute(new SendMessage(chatId,
                    title + " " + INFORMATION_MESSAGE_CATEGORY_NOT_FOUND.getMessage()));
        } else {
            List<Category> list = categoryRepository.findAllByParentId(category.getId());
            for (Category c : list) {
                categoryRepository.delete(c);
            }
            categoryRepository.delete(category);
            telegramBot.execute(new SendMessage(chatId,
                    INFORMATION_MESSAGE_CATEGORY_DELETE.getMessage() + " " + title));
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
        try {
            telegramBot.execute(new SendMessage(update.message().chat().id(),
                    INFORMATION_MESSAGE_ALL_FUNCTIONS_MENU.getMessage()));
        } catch (NullPointerException e) {
            log.info(ATTENTION.getMessage() + e);
        }


    }

    /**
     * method for structured output of information
     * to the user from database
     *
     * @param root        - start category for create category tree, title = "Категории"
     * @param f           - the line contains only dots for indentation
     * @param m           - the line contains only dots for indentation
     * @param list        - it store category title
     * @param allCategory - all categories from dataBase
     * @return List - a list with the names of categories in a structural form
     */

    @Override
    public List<String> createHierarchicalStructure(Category root, String f, String m, List<String> list, List<Category> allCategory) {
        log.info("printHierarchyTree - categoryServiceImpl");
        list.add(f + root.getTitle());
        List<Category> subs = findCategoryByParentId(root.getId(), allCategory);
        for (Category c : subs) {
            createHierarchicalStructure(c, f + m, m, list, allCategory);
        }
        return list;
    }

    /**
     * method searches for categories by parent id
     * create list with categories title
     *
     * @param rootId      - root id
     * @param allCategory - all categories from dataBase
     * @return List - a list with the categories in a structural form
     */

    public List<Category> findCategoryByParentId(long rootId, List<Category> allCategory) {
        List<Category> res = new ArrayList<>();
        for (Category c : allCategory) {
            if (c.getParentId() == rootId) {
                res.add(c);
            }
        }
        return res;
    }


    /**
     * create string  for sendMessage
     *
     * @param list - list with all titles of categories
     * @return String - this string use for send answer for user
     */
    @Override
    public String createString(List<String> list) {
        StringBuilder stb = new StringBuilder();
        for (String s : list) {
            stb.append("\t");
            stb.append("\n");
            stb.append(s);
        }
        return stb.toString();
    }


}








