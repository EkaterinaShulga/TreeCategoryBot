package com.github.EkaterinaShulga.TreeCategoryBot;

import com.github.EkaterinaShulga.TreeCategoryBot.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
public class TreeCategoryBotApplication {
         static CategoryRepository categoryRepository;
		 TreeCategoryBotApplication(){

		 }
	public static void main(String[] args) {
		SpringApplication.run(TreeCategoryBotApplication.class, args);
	}



}

