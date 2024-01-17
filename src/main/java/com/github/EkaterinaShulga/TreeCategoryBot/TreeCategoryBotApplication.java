package com.github.EkaterinaShulga.TreeCategoryBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TreeCategoryBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeCategoryBotApplication.class, args);
    }


}

