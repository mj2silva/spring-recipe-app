package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientCommandToIngredient;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
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
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId).blockOptional();
        if (recipeOptional.isEmpty()) throw new NotFoundException("Invalid recipe id");
        Recipe recipe = recipeOptional.get();
        Ingredient ingredient = recipe.getIngredients().stream().filter(ing -> Objects.equals(ing.getId(), id)).findFirst().orElse(null);
        if (ingredient == null) throw new NotFoundException("Invalid ingredient id");
        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);
        if (ingredientCommand != null) {
            ingredientCommand.setRecipeId(recipe.getId());
            return Mono.just(ingredientCommand);
        }
        return Mono.empty();
    }

    @Override
    public Mono<IngredientCommand> save(String recipeId, IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId).blockOptional();
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Invalid recipe id");
        }
        Recipe recipe = recipeOptional.get();
        Ingredient detachedIngredient = ingredientCommandToIngredient.convert(ingredientCommand);
        if (detachedIngredient == null) return null;
        if (detachedIngredient.getId() == null || Objects.equals(detachedIngredient.getId(), "")) {
            String randomIngredientId = UUID.randomUUID().toString();
            detachedIngredient.setId(randomIngredientId);
            recipe.addIngredient(detachedIngredient);
        }
        else {
            Ingredient recipeIngredient = recipe.getIngredient(detachedIngredient.getId());
            recipeIngredient.setDescription(detachedIngredient.getDescription());
            recipeIngredient.setAmount(detachedIngredient.getAmount());
            recipeIngredient.setUnitOfMeasure(detachedIngredient.getUnitOfMeasure());
        }
        recipeRepository.save(recipe).block();
        IngredientCommand savedIngredientCommand = ingredientToIngredientCommand.convert(detachedIngredient);
        if (savedIngredientCommand != null) savedIngredientCommand.setRecipeId(recipe.getId());
        return Mono.just(savedIngredientCommand);
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).block();
        if (recipe == null) throw new NotFoundException("The recipe id is invalid");
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredients.removeIf(ingredient -> Objects.equals(ingredient.getId(), ingredientId));
        recipeRepository.save(recipe).block();
        return Mono.empty();
    }
}
