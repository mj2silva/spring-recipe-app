package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.converters.RecipeCommandToRecipe;
import dev.manuelsilva.recipeapp.converters.RecipeToRecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    static final String NEW_DESCRIPTION = "New description";

    RecipeServiceImpl recipeService;
    @Mock
    RecipeReactiveRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getAllRecipes() {
        Recipe recipe = new Recipe();
        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe));

        List<Recipe> recipes = recipeService.getAllRecipes();
        assertEquals(1, recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");
        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe foundedRecipe = recipeService.getRecipeById("1L");
        assertNotNull(foundedRecipe, "Null recipe returned");
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }
    @Test
    void testGetRecipeByIdNotFound() {
        when(recipeRepository.findById(anyString())).thenReturn(Mono.empty());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> recipeService.getRecipeCommandById("1L"),
                "Expected to throw an error"
        );
        verify(recipeRepository, times(1)).findById(anyString());
        assertTrue(exception.getMessage().contains("Recipe not found"));
    }
    @Test
    void testDeleteById() {
        String idToDelete = "2L";
        when(recipeRepository.deleteById(eq(idToDelete))).thenReturn(Mono.empty());
        recipeService.deleteById(idToDelete);
        verify(recipeRepository, times(1)).deleteById(eq(idToDelete));
    }
}