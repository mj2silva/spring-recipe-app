package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    IngredientRepository ingredientRepository;
    IngredientService ingredientService;

    IngredientServiceImplTest() {
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientRepository, ingredientToIngredientCommand);
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
}