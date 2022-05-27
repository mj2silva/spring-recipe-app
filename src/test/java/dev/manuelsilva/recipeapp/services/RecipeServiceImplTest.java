package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.converters.*;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@Log4j2
class RecipeServiceImplTest {
    static final String NEW_DESCRIPTION = "New description";

    RecipeServiceImpl recipeService;
    @Mock
    RecipeReactiveRepository recipeRepository;

    RecipeToRecipeCommand recipeToRecipeCommand = new RecipeToRecipeCommand(new NotesToNotesCommand(), new CategoryToCategoryCommand(), new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()));
    RecipeCommandToRecipe recipeCommandToRecipe = new RecipeCommandToRecipe(new CategoryCommandToCategory(), new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()), new NotesCommandToNotes());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getAllRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");
        recipe.setDescription("desc");
        Recipe recipe2 = new Recipe();
        recipe2.setId("2L");
        recipe2.setDescription("desc 2");
        Flux<Recipe> recipeFlux = Flux.just(recipe, recipe2);
        when(recipeRepository.findAll()).thenReturn(recipeFlux);
        Flux<RecipeCommand> recipes = recipeService.getAllRecipes();

        StepVerifier.create(recipes)
                .expectNextMatches(rc -> rc.getId().equals("1L"))
                .expectNextMatches(rc -> rc.getId().equals("2L"))
                .expectComplete()
                .verify();

        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");
        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Mono<RecipeCommand> foundedRecipe = recipeService.getRecipeCommandById("1L");
        StepVerifier.create(foundedRecipe)
                .expectNextMatches(rc -> rc.getId().equals("1L"))
                .expectComplete()
                .verify();
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }
    @Test
    void testGetRecipeByIdNotFound() {
        when(recipeRepository.findById(anyString())).thenReturn(Mono.empty());
        Mono<RecipeCommand> recipeCommandMono = recipeService.getRecipeCommandById("1L");
        StepVerifier.create(recipeCommandMono)
                .expectComplete()
                .verify();
        verify(recipeRepository, times(1)).findById(anyString());
    }
    @Test
    void testDeleteById() {
        String idToDelete = "2L";
        when(recipeRepository.deleteById(eq(idToDelete))).thenReturn(Mono.empty());
        recipeService.deleteById(idToDelete);
        verify(recipeRepository, times(1)).deleteById(eq(idToDelete));
    }
}