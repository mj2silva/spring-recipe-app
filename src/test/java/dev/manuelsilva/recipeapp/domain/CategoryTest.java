package dev.manuelsilva.recipeapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    Category category;

    @BeforeEach
    void setUp() {
        this.category = new Category();
    }

    @Test
    void getId() {
        String mockId = "1455848484L";
        category.setId(mockId);
        assertEquals(mockId, category.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipes() {
    }
}