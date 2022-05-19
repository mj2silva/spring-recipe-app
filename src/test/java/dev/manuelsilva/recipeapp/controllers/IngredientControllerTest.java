package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.services.IngredientService;
import dev.manuelsilva.recipeapp.services.RecipeService;
import dev.manuelsilva.recipeapp.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    IngredientController controller;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListIngredients() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeService.getRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipes/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getRecipeCommandById(anyLong());
    }

    @Test
    void testShowIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findById(anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipes/1/ingredients/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    void testUpdateIngredientForm() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(1L);
        when(ingredientService.findById(anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.getAllUnitsOfMeasure()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipes/1/ingredients/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/edit"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("unitsOfMeasure"));
    }

    @Test
    void testUpdateIngredientFormWithNonExistingRecipe() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(1L);
        ingredientCommand.setId(2L);
        when(ingredientService.findById(anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.getAllUnitsOfMeasure()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipes/2/ingredients/2/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/1/ingredients/2/edit"));
    }

    @Test
    void testSaveOrUpdate() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);
        ingredientCommand.setRecipeId(2L);

        when(ingredientService.save(any(IngredientCommand.class))).thenReturn(ingredientCommand);

        mockMvc.perform(
                    post("/recipes/2/ingredients")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "some description")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/2/ingredients/1"));
    }
}