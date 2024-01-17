package com.github.EkaterinaShulga.TreeCategoryBot.servise;

import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public interface CategoryService {
    void addCategory(Update update);

    void addCategoryCategory(Update update);

    void createCategoryTree(Update update);

    void removeCategoryByTitle(Update update);

    void sendInformationAboutAllFunctions(Update update);

    List<String> createHierarchicalStructure(Category root, String f, String m, List<String> list, List<Category> allCategory);

    String createString(List<String> list);
}
