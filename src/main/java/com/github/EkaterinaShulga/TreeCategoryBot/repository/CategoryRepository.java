package com.github.EkaterinaShulga.TreeCategoryBot.repository;

import com.github.EkaterinaShulga.TreeCategoryBot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByTitle(String title);

    ArrayList<Category> findAllByParentId(Long id);

    @Query(nativeQuery = true,
            value = "select max(id) from category")
    long findMaxIdCategory();
}
