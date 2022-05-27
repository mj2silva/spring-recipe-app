package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<RecipeCommand> getAllRecipes();
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipe);
    Mono<RecipeCommand> getRecipeCommandById(String id);
    Mono<Void> deleteById(String id);
}
