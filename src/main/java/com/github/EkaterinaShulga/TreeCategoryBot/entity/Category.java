package com.github.EkaterinaShulga.TreeCategoryBot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "parent_id")
    private Long parentId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return parentId.equals(category.parentId) && Objects.equals(id, category.id) && Objects.equals(title, category.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, parentId);
    }

    @Override
    public String toString() {
        return title;
    }

}
