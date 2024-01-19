package com.github.EkaterinaShulga.TreeCategoryBot.servise;

import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import com.pengrad.telegrambot.model.Update;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    void addCategory(Update update);

    void addCategoryCategory(Update update);

    void createCategoryTree(Update update);

    void removeCategoryByTitle(Update update);

    void sendInformationAboutAllFunctions(Update update);

    List<String> createHierarchicalStructure(Category root, String f, String m, List<String> list, List<Category> allCategory);

    String createString(List<String> list);

    void sendExcelFile(Update update) throws IOException;
    Workbook createBook(List<Category> list);
    void createExcelFile(Update update, Workbook book) throws IOException;


}
