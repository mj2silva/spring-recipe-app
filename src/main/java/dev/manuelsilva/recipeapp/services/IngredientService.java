package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findById(String recipeId, String id);
    Mono<IngredientCommand> save(String recipeId, IngredientCommand ingredientCommand);
    Mono<Void> deleteById(String recipeId, String ingredientId);
}
