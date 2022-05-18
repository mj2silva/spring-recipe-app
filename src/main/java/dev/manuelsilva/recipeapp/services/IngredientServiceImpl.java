package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.converters.IngredientToIngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import dev.manuelsilva.recipeapp.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findById(Long id) {
        Ingredient detachedIngredient = ingredientRepository.findById(id).orElse(null);
        return ingredientToIngredientCommand.convert(detachedIngredient);
    }
}
