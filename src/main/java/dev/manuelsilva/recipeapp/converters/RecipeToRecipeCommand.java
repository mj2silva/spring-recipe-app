package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {
    private final NotesToNotesCommand notesConverter;
    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;

    public RecipeToRecipeCommand(NotesToNotesCommand converter, CategoryToCategoryCommand categoryConverter, IngredientToIngredientCommand ingredientConverter) {
        this.notesConverter = converter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Override
    @Synchronized
    @Nullable
    public RecipeCommand convert(@Nullable Recipe source) {
        if (source == null) return null;
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(source.getId());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setDescription(source.getDescription());
        recipeCommand.setDifficulty(source.getDifficulty());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setSource(source.getSource());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setImage(source.getImage());
        if (source.getNotes() != null) recipeCommand.setNotesCommand(notesConverter.convert(source.getNotes()));
        if (source.getCategories() != null && source.getCategories().size() > 0) {
            source
                    .getCategories()
                    .forEach(category -> recipeCommand.getCategories().add(categoryConverter.convert(category)));
        }
        if (source.getIngredients() != null && source.getIngredients().size() > 0) {
            source
                    .getIngredients()
                    .forEach(ingredientCommand -> recipeCommand.getIngredients().add(ingredientConverter.convert(ingredientCommand)));
        }
        return recipeCommand;
    }
}
