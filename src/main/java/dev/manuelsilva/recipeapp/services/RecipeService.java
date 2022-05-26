package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Recipe getRecipeById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand recipe);
    RecipeCommand getRecipeCommandById(String id);
    void deleteById(String id);
}
