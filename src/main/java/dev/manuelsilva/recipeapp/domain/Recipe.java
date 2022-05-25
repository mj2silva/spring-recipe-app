package dev.manuelsilva.recipeapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"notes", "ingredients"})
public class Recipe {
    private String id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;
    private Byte[] image;
    private Notes notes;
    private Set<Ingredient> ingredients;
    private Set<Category> categories;

    public void setNotes(Notes notes) {
        notes.setRecipe(this);
        this.notes = notes;
    }

    public Set<Ingredient> addIngredient(Ingredient ingredient) {
        if (this.ingredients == null) this.ingredients = new HashSet<>();
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this);
        return this.ingredients;
    }

    public Set<Category> addCategory(Category category) {
        if (this.categories == null) this.categories = new HashSet<>();
        this.categories.add(category);
        return this.categories;
    }
}
