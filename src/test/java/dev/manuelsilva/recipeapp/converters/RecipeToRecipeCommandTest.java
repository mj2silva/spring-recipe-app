package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.CategoryCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {
    static final Long RECIPE_ID = 1L;
    static final Integer COOK_TIME = 5;
    static final Integer PREP_TIME = 7;
    static final String DESCRIPTION = "My test recipe";
    static final String DIRECTIONS = "Just put it all in the oven!";
    static final Difficulty DIFFICULTY = Difficulty.EASY;
    static final Integer SERVINGS = 3;
    static final String SOURCE = "Nowhere";
    static final String URL = "https://thisisafakeurlandshouldnotbeused.com";
    static final Long CAT_1_ID = 1L;
    static final Long CAT_2_ID = 2L;
    static final Long INGREDIENT_1_ID = 1L;
    static final Long INGREDIENT_2_ID = 2L;
    static final Long NOTES_ID = 1L;

    RecipeToRecipeCommand converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeToRecipeCommand(
                new NotesToNotesCommand(),
                new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand())
        );
    }

    @Test
    public void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    void convert() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setDescription(DESCRIPTION);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDirections(DIRECTIONS);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);
        Ingredient ingredient1 = new Ingredient();
        Ingredient ingredient2 = new Ingredient();
        ingredient1.setId(INGREDIENT_1_ID);
        ingredient2.setId(INGREDIENT_2_ID);
        Category category1 = new Category();
        Category category2 = new Category();
        category1.setId(CAT_1_ID);
        category2.setId(CAT_2_ID);
        Notes notes = new Notes();
        notes.setId(NOTES_ID);

        // When
        RecipeCommand recipeCommand = converter.convert(recipe);

        // Then
        assertNotNull(recipeCommand);
        assertEquals(RECIPE_ID, recipeCommand.getId());
        assertEquals(DESCRIPTION, recipeCommand.getDescription());
        assertEquals(COOK_TIME, recipeCommand.getCookTime());
        assertEquals(PREP_TIME, recipeCommand.getPrepTime());
        assertEquals(DIRECTIONS, recipeCommand.getDirections());
        assertEquals(DIFFICULTY, recipeCommand.getDifficulty());
        assertEquals(SERVINGS, recipeCommand.getServings());
        assertEquals(SOURCE, recipeCommand.getSource());
        assertEquals(URL, recipeCommand.getUrl());
    }
}