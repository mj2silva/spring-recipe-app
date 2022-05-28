package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.services.IngredientService;
import dev.manuelsilva.recipeapp.services.RecipeService;
import dev.manuelsilva.recipeapp.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebFluxTest(IngredientController.class)
@ExtendWith(SpringExtension.class)
class IngredientControllerTest {
    @MockBean
    RecipeService recipeService;
    @MockBean
    IngredientService ingredientService;
    @MockBean
    UnitOfMeasureService unitOfMeasureService;
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListIngredients() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1L");
        IngredientCommand ingredient1 = new IngredientCommand();
        ingredient1.setId("2L");
        IngredientCommand ingredient2 = new IngredientCommand();
        ingredient2.setId("3L");
        recipeCommand.getIngredients().add(ingredient1);
        recipeCommand.getIngredients().add(ingredient2);

        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

        webTestClient
                .get()
                .uri("/recipes/1L/ingredients")
                .exchange().expectStatus().isOk();

        verify(recipeService, times(1)).getRecipeCommandById(anyString());
    }

    @Test
    void testShowIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId("1L");
        when(ingredientService.findById(eq("1L"), anyString())).thenReturn(Mono.just(ingredientCommand));

        webTestClient
                .get()
                .uri("/recipes/1L/ingredients/2")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testUpdateIngredientForm() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId("1L");
        when(ingredientService.findById(eq("1L"), anyString())).thenReturn(Mono.just(ingredientCommand));
        when(unitOfMeasureService.getAllUnitsOfMeasure()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/recipes/1L/ingredients/2L/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testUpdateIngredientFormWithNonExistingRecipe() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId("1L");
        ingredientCommand.setId("2L");
        when(ingredientService.findById(eq("2L"), anyString())).thenReturn(Mono.error(new NotFoundException("Recipe not found")));

        webTestClient
                .get()
                .uri("/recipes/2L/ingredients/2L/edit")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testSaveNewIngredientForm() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId("2L");

        when(recipeService.getRecipeCommandById(eq("2L"))).thenReturn(Mono.just(recipe));
        when(unitOfMeasureService.getAllUnitsOfMeasure()).thenReturn(Flux.empty());

        webTestClient
                .get()
                .uri("/recipes/2L/ingredients/new")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testSaveUpdateIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("1L");
        ingredientCommand.setRecipeId("2L");

        when(ingredientService.save(eq("2L"), any(IngredientCommand.class))).thenReturn(Mono.just(ingredientCommand));

        webTestClient
                .post()
                .uri("/recipes/2L/ingredients")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters
                            .fromFormData("id", "1L")
                            .with("description", "Some description")
                )
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void testSaveNewIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("1L");
        ingredientCommand.setRecipeId("2L");

        when(ingredientService.save(eq("2L"), any(IngredientCommand.class))).thenReturn(Mono.just(ingredientCommand));

        webTestClient
                .post()
                .uri("/recipes/2L/ingredients")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters
                                .fromFormData("id", "")
                                .with("description", "Some description")
                )
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void testDeleteIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("1L");
        ingredientCommand.setRecipeId("2L");

        when(ingredientService.deleteById(eq("2L"), eq("1L"))).thenReturn(Mono.empty());

        webTestClient
                .get()
                .uri("/recipes/2L/ingredients/1L/delete")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}