package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;

public interface IngredientService {
    IngredientCommand findById(Long id);
}
