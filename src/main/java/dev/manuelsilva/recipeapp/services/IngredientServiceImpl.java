package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientCommandToIngredient;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.IngredientRepository;
import dev.manuelsilva.recipeapp.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findById(Long id) {
        Ingredient detachedIngredient = ingredientRepository.findById(id).orElse(null);
        return ingredientToIngredientCommand.convert(detachedIngredient);
    }

    @Override
    @Transactional
    public IngredientCommand save(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());
        if (recipeOptional.isEmpty()) return null;
        Ingredient detachedIngredient = ingredientCommandToIngredient.convert(ingredientCommand);
        if (detachedIngredient == null) return null;
        detachedIngredient.setRecipe(recipeOptional.get());
        Ingredient savedIngredient = ingredientRepository.save(detachedIngredient);
        return ingredientToIngredientCommand.convert(savedIngredient);
    }

    @Override
    public void deleteById(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }
}
