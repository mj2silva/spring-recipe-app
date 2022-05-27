package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import org.springframework.transaction.annotation.Transactional;

public interface IngredientService {
    IngredientCommand findById(String recipeId, String id);
    @Transactional
    IngredientCommand save(String recipeId, IngredientCommand ingredientCommand);
    void deleteById(String recipeId, String ingredientId);
}
