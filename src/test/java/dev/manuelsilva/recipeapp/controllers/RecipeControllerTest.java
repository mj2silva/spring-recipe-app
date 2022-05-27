package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
class RecipeControllerTest {
    RecipeController recipeController;
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    @Captor
    ArgumentCaptor<List<Recipe>> recipesCaptor;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(recipeController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    void testMockMvc() throws Exception {
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/index"));
    }

    @Test
    void getRecipes() {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId("1L");
        RecipeCommand recipe2 = new RecipeCommand();
        recipe2.setId("2L");
        when(recipeService.getAllRecipes()).thenReturn(Flux.just(recipe, recipe2));

        assertEquals("recipes/index", recipeController.getRecipes(model));
        verify(model, times(1)).addAttribute(eq("recipes"), recipesCaptor.capture());
        verify(recipeService, times(1)).getAllRecipes();
        List<Recipe> capturedRecipes = recipesCaptor.getValue();
        assertEquals(2, capturedRecipes.size());
    }

    @Test
    void getRecipe() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId("1L");

        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(recipe));

        mockMvc
                .perform(get("/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/show"))
                .andExpect(model().attributeExists("recipe"));
    }
    @Test
    void getNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void postNewRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(Mono.just(command));
        mockMvc.perform(
                post("/recipes")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "Some description")
                        .param("directions", "do some stuffs")
            )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/2L"));
    }

    @Test
    void postEmptyRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(Mono.just(command));
        mockMvc.perform(
                        post("/recipes")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "")
                                .param("description", "")
                                .param("directions", "")
                                .param("cookTime", "")
                                .param("difficulty", "EASY")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/form"));
    }

    @Test
    void testGetUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");
        when(recipeService.getRecipeCommandById(eq("2L"))).thenReturn(Mono.just(command));
        mockMvc.perform(get("/recipes/2L/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipes/1L/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/"));

        verify(recipeService, times(1)).deleteById(anyString());
    }

    @Test
    void testChangeRecipeImageForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1L");

        when(recipeService.getRecipeCommandById(eq("1L"))).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(get("/recipes/1L/change-image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getRecipeCommandById(anyString());
    }


    @Test
    @Disabled
    void testRenderImageFromDB() throws Exception {
        /*RecipeCommand command = new RecipeCommand();
        command.setId("1L");
        String fakeImage = "Some string to replace a non existing image u.u";
        byte[] primitiveBytesFromImage = fakeImage.getBytes();
        Byte[] bytesFromImage = new Byte[primitiveBytesFromImage.length];
        for (int i = 0; i < primitiveBytesFromImage.length; i++) {
            bytesFromImage[i] = primitiveBytesFromImage[i];
        }
        command.setImage(bytesFromImage);
        when(recipeService.getRecipeCommandById(eq("1L"))).thenReturn(command);
        MockHttpServletResponse response = mockMvc.perform(get("/recipes/1L/image"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        byte[] bytesFromResponse = response.getContentAsByteArray();
        assertEquals(primitiveBytesFromImage.length, bytesFromResponse.length);*/
    }

    @Test
    void testRecipeNotFoundHandler() throws Exception {
        when(recipeService.getRecipeCommandById(anyString())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipes/1L"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("errors/404"));
    }
}