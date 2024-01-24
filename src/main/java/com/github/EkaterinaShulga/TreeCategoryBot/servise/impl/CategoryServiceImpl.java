package com.github.EkaterinaShulga.TreeCategoryBot.servise.impl;


import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import com.github.EkaterinaShulga.TreeCategoryBot.repository.CategoryRepository;
import com.github.EkaterinaShulga.TreeCategoryBot.servise.CategoryService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.github.EkaterinaShulga.TreeCategoryBot.constants.TextResponsesForUser.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TelegramBot telegramBot;

    @Value("${path.to.file.folder}")
    private String fileDis;

    @Value("${telegram.bot.token}")
    private String token;

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

    /**
     * method requests information from the database,
     * if database is empty - notifies the user about this
     * else - calls the method for creating an Excel file and sends
     * it to the user
     *
     * @param update - update from the bot
     */
    @Override
    public void sendExcelFile(Update update) throws IOException {
        log.info("sendExcelFile - categoryServiceImpl");
        long chatId = update.message().chat().id();
        List<Category> list = categoryRepository.findAll();
        if (list.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, INFORMATION_MESSAGE_DATABASE_IS_IMPTY.getMessage()));
        } else {
            createExcelFile(update, createBook(list));
        }
    }

    /**
     * method creates book for information from database
     *
     * @param list - list with all titles of categories
     * @return Workbook - book with information from database
     */
    @Override
    public Workbook createBook(List<Category> list) {
        log.info("createBook - categoryServiceImpl");
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("CategoryTree");
        for (int i = 0, y = 1; i < list.size(); i++, y++) {
            Row rowTitle = sheet.createRow(0);
            Cell cellIdCategory = rowTitle.createCell(0);
            cellIdCategory.setCellValue("id category");
            Cell cellTitleCategory = rowTitle.createCell(1);
            cellTitleCategory.setCellValue("name category");
            Cell cellParentIdCategory = rowTitle.createCell(2);
            cellParentIdCategory.setCellValue("id parent");

            Row rowData = sheet.createRow(y);
            Cell cellId = rowData.createCell(0);
            Cell cellTitle = rowData.createCell(1);
            Cell cellParentId = rowData.createCell(2);
            cellId.setCellValue(list.get(i).getId());
            cellTitle.setCellValue(list.get(i).getTitle());
            cellParentId.setCellValue(list.get(i).getParentId());
        }
        return book;

    }

    /**
     * method creates Excel file with information from database,
     * sends file for user
     *
     * @param update- - update from the bot
     * @param book    - book with information from database
     */
    public void createExcelFile(Update update, Workbook book) throws IOException {
        log.info("createExcelFile - categoryServiceImpl");
        long chatId = update.message().chat().id();
        try
                (FileOutputStream fileOut = new FileOutputStream(fileDis)) {
            book.write(fileOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] byteBook = Files.readAllBytes(Path.of(fileDis));
        SendDocument sendDocument = new SendDocument(chatId, byteBook);

        telegramBot.execute(new SendMessage(chatId, INFORMATION_MESSAGE_CREATE_EXCEL_FILE.getMessage()));
        telegramBot.execute(sendDocument);
    }

    /**
     * method accepts Excel file from user,
     * downloads it to the directory
     * sends a response to the user
     *
     * @param update- - update from the bot
     */
    public void uploadExcelFile(Update update) {
        log.info("uploadExcelFile - categoryServiceImpl");
        String outputFileName = "src/main/resources/updates/files/book.xls";
        String fileId = update.message().document().fileId();
        URL url;
        try {
            url = new URL("https://api.telegram.org/bot" + token + "/" + "getFile?file_id=" + fileId);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String getFileResponse = buffer.readLine();
            JsonElement jResult = JsonParser.parseString(getFileResponse);
            JsonObject path = jResult.getAsJsonObject().getAsJsonObject("result");
            String file_path = path.get("file_path").getAsString();
            File localFile = new File(outputFileName);
            try (InputStream is = new URL("https://api.telegram.org/file/bot"
                    + token + "/" + file_path).openStream()) {
                FileUtils.copyInputStreamToFile(is, localFile);
                createExcelFileForDataBase(update);
                buffer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * the method creates a file and saves information
     * from the loaded file in it, then reads information
     * from it and saves it in the database
     *
     * @param update- - update from the bot
     */
    @Override
    public void createExcelFileForDataBase(Update update) {
        log.info("createExcelFileForDataBase - categoryServiceImpl");
        String outputFileName = "src/main/resources/updates/files/book.xls";
        try (FileInputStream fileOut = new FileInputStream(outputFileName)) {
            Workbook book = new HSSFWorkbook(fileOut);
            for (Row row : book.getSheetAt(0)) {
                Category category = createCategoryFromUploadFile(saveInformationFromExcelFileToList(row));
                categoryRepository.save(category);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * the method reads information from the Workbook
     * line passed to it and converts it into a String,
     * saving Strings into a List<String>
     * each String is information about one category
     *
     * @param row - row from Workbook
     * @return List
     */
    @Override
    public List<String> saveInformationFromExcelFileToList(Row row) {
        List<String> result = new ArrayList<>();
        for (Cell cell : row) {
            switch (cell.getCellType()) {
                case STRING -> result.add(cell.getRichStringCellValue().getString());
                case NUMERIC -> {
                    long l = Math.round(cell.getNumericCellValue());
                    String s = String.valueOf(l);
                    result.add(s);
                }
            }
        }

        return result;
    }

    /**
     * the method takes a List with String
     * and converts each String into a category
     *
     * @param list - list with Strings
     * @return Category - from upload file
     */
    @Override
    public Category createCategoryFromUploadFile(List<String> list) {
        Category category = new Category();
        category.setId(Long.parseLong(list.get(0)));
        category.setTitle(list.get(1));
        category.setParentId(Long.parseLong(list.get(2)));

        return category;
    }


}







