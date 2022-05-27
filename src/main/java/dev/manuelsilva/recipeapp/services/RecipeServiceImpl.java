package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.converters.RecipeCommandToRecipe;
import dev.manuelsilva.recipeapp.converters.RecipeToRecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<RecipeCommand> getAllRecipes() {
        Flux<Recipe> recipeFlux = recipeRepository.findAll();
        return recipeFlux.mapNotNull(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipe) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipe);
        if (detachedRecipe != null) {
            Mono<Recipe> savedRecipe = recipeRepository.save(detachedRecipe);
            return savedRecipe.mapNotNull(recipeToRecipeCommand::convert);
        }
        return Mono.empty();
    }

    @Override
    public Mono<RecipeCommand> getRecipeCommandById(String id) {
        Mono<Recipe> recipeFlux = recipeRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));
        return recipeFlux.mapNotNull(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeRepository.deleteById(id);
    }
}
