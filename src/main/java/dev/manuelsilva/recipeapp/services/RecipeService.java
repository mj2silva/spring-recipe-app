package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();
    Recipe getRecipeById(Long id);
}
