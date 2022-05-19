package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientCommandToIngredient;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.IngredientRepository;
import dev.manuelsilva.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {
    static Long RECIPE_ID = 1L;
    static Long INGREDIENT_ID = 2L;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    IngredientRepository ingredientRepository;
    IngredientService ingredientService;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    IngredientServiceImplTest() {
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientRepository, recipeRepository, ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    void testFindById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient_1 = new Ingredient();
        ingredient_1.setId(1L);
        Ingredient ingredient_2 = new Ingredient();
        ingredient_2.setId(2L);
        Ingredient ingredient_3 = new Ingredient();
        ingredient_3.setId(3L);
        recipe.addIngredient(ingredient_1);
        recipe.addIngredient(ingredient_2);
        recipe.addIngredient(ingredient_3);
        when(ingredientRepository.findById(eq(1L))).thenReturn(Optional.of(ingredient_1));
        when(ingredientRepository.findById(eq(2L))).thenReturn(Optional.of(ingredient_2));
        when(ingredientRepository.findById(eq(3L))).thenReturn(Optional.of(ingredient_3));

        IngredientCommand ingredientCommand_1 = ingredientService.findById(1L);
        assertEquals(1L, ingredientCommand_1.getId());
        assertEquals(1L, ingredientCommand_1.getRecipeId());
    }

    @Test
    void saveIngredientCommand() {
        Recipe savedRecipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        IngredientCommand ingredientCommand = new IngredientCommand();

        savedRecipe.setId(RECIPE_ID);
        ingredient.setId(INGREDIENT_ID);
        savedRecipe.addIngredient(ingredient);

        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setRecipeId(RECIPE_ID);

        when(recipeRepository.findById(eq(RECIPE_ID))).thenReturn(Optional.of(savedRecipe));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        IngredientCommand savedCommand = ingredientService.save(ingredientCommand);

        assertEquals(INGREDIENT_ID, savedCommand.getId());
        assertEquals(RECIPE_ID, savedCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void saveIngredientCommandWithNonExistingRecipe() {
        Ingredient ingredient = new Ingredient();
        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setRecipeId(RECIPE_ID);

        when(recipeRepository.findById(eq(RECIPE_ID))).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        IngredientCommand savedCommand = ingredientService.save(ingredientCommand);

        assertNull(savedCommand);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(ingredientRepository, times(0)).save(any(Ingredient.class));
    }
}