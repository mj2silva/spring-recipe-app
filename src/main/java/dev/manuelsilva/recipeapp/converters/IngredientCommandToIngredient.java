package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureConverter;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureConverter) {
        this.unitOfMeasureConverter = unitOfMeasureConverter;
    }

    @Override
    @Nullable
    @Synchronized
    public Ingredient convert(@Nullable IngredientCommand source) {
        if (source == null) return null;
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setUnitOfMeasure(unitOfMeasureConverter.convert(source.getUnitOfMeasure()));
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        return ingredient;
    }
}
