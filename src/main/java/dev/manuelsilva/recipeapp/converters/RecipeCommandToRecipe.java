package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.domain.Notes;
import dev.manuelsilva.recipeapp.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConverter, IngredientCommandToIngredient ingredientConverter, NotesCommandToNotes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(@Nullable RecipeCommand source) {
        if (source == null) return null;
        final Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDirections(source.getDirections());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        Notes notes = notesConverter.convert(source.getNotesCommand());
        if (notes != null) recipe.setNotes(notes);
        if (source.getCategories() != null && source.getCategories().size() > 0) {
            source
                    .getCategories()
                    .forEach(categoryCommand -> recipe.addCategory(categoryConverter.convert(categoryCommand)));
        }
        if (source.getIngredients() != null && source.getIngredients().size() > 0) {
            source
                    .getIngredients()
                    .forEach(ingredientCommand -> recipe.addIngredient(ingredientConverter.convert(ingredientCommand)));
        }
        return recipe;
    }
}
