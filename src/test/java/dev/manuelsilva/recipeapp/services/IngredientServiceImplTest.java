package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientCommandToIngredient;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {
    static String RECIPE_ID = "1L";
    static String INGREDIENT_ID = "2L";
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    RecipeReactiveRepository recipeRepository;
    IngredientService ingredientService;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    IngredientServiceImplTest() {
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    void testFindById() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        Ingredient ingredient_1 = new Ingredient();
        ingredient_1.setId("1L");
        Ingredient ingredient_2 = new Ingredient();
        ingredient_2.setId("2L");
        Ingredient ingredient_3 = new Ingredient();
        ingredient_3.setId("3L");
        recipe.addIngredient(ingredient_1);
        recipe.addIngredient(ingredient_2);
        recipe.addIngredient(ingredient_3);
        when(recipeRepository.findById(eq("1L"))).thenReturn(Mono.just(recipe));

        IngredientCommand ingredientCommand_1 = ingredientService.findById("1L", "1L");
        assertEquals("1L", ingredientCommand_1.getId());
        assertEquals("1L", ingredientCommand_1.getRecipeId());
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

        when(recipeRepository.findById(eq(RECIPE_ID))).thenReturn(Mono.just(savedRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(Mono.just(savedRecipe));

        IngredientCommand savedCommand = ingredientService.save(RECIPE_ID, ingredientCommand);

        assertEquals(INGREDIENT_ID, savedCommand.getId());
        assertEquals(RECIPE_ID, savedCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    void saveIngredientCommandWithNonExistingRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        when(recipeRepository.findById(eq("NOT_VALID_ID"))).thenReturn(Mono.empty());
        when(recipeRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> ingredientService.save("NOT_VALID_ID", ingredientCommand),
                "Expected to throw an error"
        );
        verify(recipeRepository, times(1)).findById(anyString());
        assertTrue(exception.getMessage().contains("Invalid recipe id"));
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(0)).save(any(Recipe.class));
    }

    @Test
    void deleteById() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");
        List<Ingredient> ingredientList = new ArrayList<>();
        recipe.setIngredients(ingredientList);
        when(recipeRepository.findById(eq("1L"))).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));
        ingredientService.deleteById("1L", "1L");
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(recipeRepository, times(1)).findById(eq("1L"));
    }
}