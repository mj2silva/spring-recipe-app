package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class RecipeControllerTest {
    RecipeController recipeController;
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    @Captor
    ArgumentCaptor<Set<Recipe>> recipesCaptor;
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
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        HashSet<Recipe> recipes = new HashSet<>();
        recipes.add(recipe);
        recipes.add(recipe2);
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        assertEquals("recipes/index", recipeController.getRecipes(model));
        verify(model, times(1)).addAttribute(eq("recipes"), recipesCaptor.capture());
        verify(recipeService, times(1)).getAllRecipes();
        Set<Recipe> capturedRecipes = recipesCaptor.getValue();
        assertEquals(2, capturedRecipes.size());
    }

    @Test
    void getRecipe() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);

        when(recipeService.getRecipeCommandById(anyLong())).thenReturn(recipe);

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
        command.setId(2L);
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(command);
        mockMvc.perform(
                post("/recipes")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "Some description")
                        .param("directions", "do some stuffs")
            )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/2"));
    }

    @Test
    void postEmptyRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(command);
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
        command.setId(2L);
        when(recipeService.getRecipeCommandById(eq(2L))).thenReturn(command);
        mockMvc.perform(get("/recipes/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipes/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }

    @Test
    void testChangeRecipeImageForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.getRecipeCommandById(eq(1L))).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipes/1/change-image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getRecipeCommandById(anyLong());
    }


    @Test
    void testRenderImageFromDB() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        String fakeImage = "Some string to replace a non existing image u.u";
        byte[] primitiveBytesFromImage = fakeImage.getBytes();
        Byte[] bytesFromImage = new Byte[primitiveBytesFromImage.length];
        for (int i = 0; i < primitiveBytesFromImage.length; i++) {
            bytesFromImage[i] = primitiveBytesFromImage[i];
        }
        command.setImage(bytesFromImage);
        when(recipeService.getRecipeCommandById(eq(1L))).thenReturn(command);
        MockHttpServletResponse response = mockMvc.perform(get("/recipes/1/image"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        byte[] bytesFromResponse = response.getContentAsByteArray();
        assertEquals(primitiveBytesFromImage.length, bytesFromResponse.length);
    }

    @Test
    void testRecipeNotFoundHandler() throws Exception {
        when(recipeService.getRecipeCommandById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("errors/404"));
    }

    @Test
    void testRecipeWithWrongIdFormat() throws Exception {
        // Notice the given id is not a number long like value, so the route should return a bad request response
        mockMvc.perform(get("/recipes/wrong-id"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("errors/400WrongId"));
    }
}