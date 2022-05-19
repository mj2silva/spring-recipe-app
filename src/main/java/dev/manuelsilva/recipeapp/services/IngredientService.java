package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findById(Long id);
    IngredientCommand save(IngredientCommand ingredientCommand);

    void deleteById(Long ingredientId);
}
