package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RecipeControllerTest {
    RecipeController recipeController;
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    @Captor
    ArgumentCaptor<Set<Recipe>> recipesCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(recipeService);
    }

    @Test
    void testMockMvc() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
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
}