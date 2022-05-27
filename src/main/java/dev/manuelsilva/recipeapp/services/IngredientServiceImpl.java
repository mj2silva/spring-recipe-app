package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientCommandToIngredient;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class IngredientServiceImpl implements IngredientService {
    private final RecipeReactiveRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findById(String recipeId, String id) {
        Mono<Recipe> recipeMono = recipeRepository
                .findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));
        return recipeMono.mapNotNull(recipe -> ingredientToIngredientCommand.convert(recipe.getIngredient(id)));
    }

    @Override
    public Mono<IngredientCommand> save(String recipeId, IngredientCommand ingredientCommand) {
        Mono<Recipe> recipeOptional = recipeRepository
                .findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));
        Ingredient detachedIngredient = ingredientCommandToIngredient.convert(ingredientCommand);
        if (detachedIngredient == null) return Mono.empty();
        return recipeOptional
                .map(recipe -> {
                    if (detachedIngredient.getId() == null || Objects.equals(detachedIngredient.getId(), "")) {
                        String randomIngredientId = UUID.randomUUID().toString();
                        detachedIngredient.setId(randomIngredientId);
                        recipe.addIngredient(detachedIngredient);
                    } else {
                        Ingredient recipeIngredient = recipe.getIngredient(detachedIngredient.getId());
                        recipeIngredient.setDescription(detachedIngredient.getDescription());
                        recipeIngredient.setAmount(detachedIngredient.getAmount());
                        recipeIngredient.setUnitOfMeasure(detachedIngredient.getUnitOfMeasure());
                    }
                    return recipe;
                })
                .flatMap(recipeRepository::save)
                .mapNotNull(savedRecipe -> ingredientToIngredientCommand.convert(savedRecipe.getIngredient(detachedIngredient.getId())));
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Mono<Recipe> recipeMono = recipeRepository
                .findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found")));
        return recipeMono
            .map(recipe -> {
                List<Ingredient> ingredients = recipe.getIngredients();
                ingredients.removeIf(ingredient -> Objects.equals(ingredient.getId(), ingredientId));
                return recipe;
            })
            .flatMap(recipeRepository::save)
            .doOnError(throwable -> log.error(throwable.getMessage()))
            .then(Mono.empty());
    }
}
