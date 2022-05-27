package dev.manuelsilva.recipeapp.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
@EqualsAndHashCode(exclude = {"notes", "ingredients"})
public class Recipe {
    @MongoId(FieldType.OBJECT_ID)
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
    private List<Ingredient> ingredients;
    @DBRef
    private List<Category> categories;

    public List<Ingredient> addIngredient(Ingredient ingredient) {
        if (this.ingredients == null) this.ingredients = new ArrayList<>();
        this.ingredients.add(ingredient);
        // ingredient.setRecipe(this);
        return this.ingredients;
    }
    public Ingredient getIngredient(String ingredientId) {
        if (this.ingredients == null) return null;
        return this.ingredients
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .orElse(null);
    }

    public List<Category> addCategory(Category category) {
        if (this.categories == null) this.categories = new ArrayList<>();
        this.categories.add(category);
        return this.categories;
    }
}
