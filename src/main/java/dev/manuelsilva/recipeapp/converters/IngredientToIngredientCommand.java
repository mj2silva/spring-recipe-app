package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureConverter;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureConverter) {
        this.unitOfMeasureConverter = unitOfMeasureConverter;
    }

    @Override
    @Nullable
    @Synchronized
    public IngredientCommand convert(@Nullable Ingredient source) {
        if (source == null) return null;
        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(source.getId());
        ingredientCommand.setUnitOfMeasure(unitOfMeasureConverter.convert(source.getUom()));
        ingredientCommand.setAmount(source.getAmount());
        ingredientCommand.setDescription(source.getDescription());
        if (source.getRecipe() != null) ingredientCommand.setRecipeId(source.getRecipe().getId());
        return ingredientCommand;
    }
}
