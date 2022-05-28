package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebFluxTest(RecipeController.class)
@ExtendWith(SpringExtension.class)
class RecipeControllerTest {
    @MockBean
    RecipeService recipeService;
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMockMvc() throws Exception {
        webTestClient.get().uri("/recipes")
                        .exchange()
                        .expectStatus()
                        .isOk();
    }

    @Test
    void getRecipes() {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId("1L");
        RecipeCommand recipe2 = new RecipeCommand();
        recipe2.setId("2L");
        when(recipeService.getAllRecipes()).thenReturn(Flux.just(recipe, recipe2));
    }

    @Test
    void getRecipe() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId("1L");

        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(recipe));

        webTestClient
                .get()
                .uri("/recipes/1L")
                .exchange()
                .expectStatus()
                .isOk();
    }
    @Test
    void getNewRecipeForm() throws Exception {
        webTestClient
                .get()
                .uri("/recipes/new")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void postNewRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(Mono.just(command));
        webTestClient
                .post()
                .uri("/recipes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("id", "")
                        .with("description", "Some description")
                        .with("directions", "do some stuffs")
                )
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void postEmptyRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(Mono.just(command));
        webTestClient
                .post()
                .uri("/recipes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("id", "")
                        .with("description", "")
                        .with("directions", "")
                        .with("difficulty", "EASY")
                )
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testGetUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");
        when(recipeService.getRecipeCommandById(eq("2L"))).thenReturn(Mono.just(command));
        webTestClient
                .get()
                .uri("/recipes/2L/update")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testDeleteAction() throws Exception {
        webTestClient
                .get()
                .uri("/recipes/1L/delete")
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        verify(recipeService, times(1)).deleteById(anyString());
    }

    @Test
    void testChangeRecipeImageForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1L");

        when(recipeService.getRecipeCommandById(eq("1L"))).thenReturn(Mono.just(recipeCommand));

        webTestClient
                .get()
                .uri("/recipes/1L/change-image")
                .exchange()
                .expectStatus()
                .isOk();

        verify(recipeService, times(1)).getRecipeCommandById(anyString());
    }


    @Test
    void testRenderImageFromDB() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("1L");
        String fakeImage = "Some string to replace a non existing image u.u";
        byte[] primitiveBytesFromImage = fakeImage.getBytes();
        Byte[] bytesFromImage = new Byte[primitiveBytesFromImage.length];
        for (int i = 0; i < primitiveBytesFromImage.length; i++) {
            bytesFromImage[i] = primitiveBytesFromImage[i];
        }
        command.setImage(bytesFromImage);
        when(recipeService.getRecipeCommandById(eq("1L"))).thenReturn(Mono.just(command));

        EntityExchangeResult<byte[]> response = webTestClient
                .get()
                .uri("/recipes/1L/image")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .returnResult();

        byte[] bytesFromResponse = response.getResponseBodyContent();
        assertNotNull(bytesFromResponse);
        assertEquals(primitiveBytesFromImage.length, bytesFromResponse.length);
    }

    @Test
    void testRecipeNotFoundHandler() throws Exception {
        when(recipeService.getRecipeCommandById(anyString())).thenThrow(NotFoundException.class);
        webTestClient
                .get()
                .uri("/recipes/1L/image")
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}