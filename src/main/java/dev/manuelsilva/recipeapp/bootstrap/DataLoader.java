package dev.manuelsilva.recipeapp.bootstrap;

import dev.manuelsilva.recipeapp.domain.*;
import dev.manuelsilva.recipeapp.repositories.CategoryRepository;
import dev.manuelsilva.recipeapp.repositories.RecipeRepository;
import dev.manuelsilva.recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        // If there are not already in the database, units of measure are created
        UnitOfMeasure ripe = getUnitOfMeasureOrCreateIfNotExists("Ripe");
        UnitOfMeasure teaspoon = getUnitOfMeasureOrCreateIfNotExists("Teaspoon");
        UnitOfMeasure tablespoon = getUnitOfMeasureOrCreateIfNotExists("Tablespoon");
        UnitOfMeasure pieces = getUnitOfMeasureOrCreateIfNotExists("Piece");
        UnitOfMeasure pinch = getUnitOfMeasureOrCreateIfNotExists("Pinch");
        UnitOfMeasure slices = getUnitOfMeasureOrCreateIfNotExists("Slices");
        UnitOfMeasure pounds = getUnitOfMeasureOrCreateIfNotExists("Pounds");

        // Retrieving categories
        Category mexican = getCategoryOrCreateIfNotExists("Mexican");
        Category fastFood = getCategoryOrCreateIfNotExists("Fast food");
        Category american = getCategoryOrCreateIfNotExists("American");

        // Starting to create the Perfect Guacamole
        createPerfectGuacamoleRecipe(ripe, teaspoon, tablespoon, pieces, pinch, slices, mexican, fastFood);

        // Starting to prep the Spicy Chicken Grilled Tacos
        createSpicyChickenTacosRecipe(ripe, pounds, teaspoon, tablespoon, pieces, mexican, fastFood, american);
    }

    private void createPerfectGuacamoleRecipe(
            UnitOfMeasure ripe,
            UnitOfMeasure teaspoon,
            UnitOfMeasure tablespoon,
            UnitOfMeasure pieces,
            UnitOfMeasure pinch,
            UnitOfMeasure slices,
            Category mexican,
            Category fastFood
    ) {
        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes(
                "Be careful handling chilis! If using, it's best to wear food-safe gloves. " +
                "If no gloves are available, wash your hands thoroughly after handling, and " +
                "do not touch your eyes or the area near your eyes for several hours afterwards."
        );

        Recipe perfectGuacamole = new Recipe();

        perfectGuacamole.setDescription("The Best Guacamole");
        perfectGuacamole.setPrepTime(10);
        perfectGuacamole.setCookTime(0);
        perfectGuacamole.setServings(4);
        perfectGuacamole.setSource("simplyrecipes.com");
        perfectGuacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        perfectGuacamole.setDirections(
                """
                1. Cut the Avocado.
                2. Mash the avocado flesh
                3. Add the remaining ingredients to taste
                4. Serve immediately
                """
        );
        perfectGuacamole.setDifficulty(Difficulty.EASY);
        perfectGuacamole.setNotes(guacamoleNotes);
        guacamoleNotes.setRecipe(perfectGuacamole);
        addIngredientToRecipe("avocado", ripe,2F, perfectGuacamole);
        addIngredientToRecipe("kosher salt", teaspoon,0.25F, perfectGuacamole);
        addIngredientToRecipe("lemon juice", tablespoon,1F, perfectGuacamole);
        addIngredientToRecipe("chilis", pieces,4F, perfectGuacamole);
        addIngredientToRecipe("cilantro", tablespoon,2F, perfectGuacamole);
        addIngredientToRecipe("black pepper", pinch,1F, perfectGuacamole);
        addIngredientToRecipe("tomato", ripe,0.5F, perfectGuacamole);
        addIngredientToRecipe("red radish", slices,4F, perfectGuacamole);
        addIngredientToRecipe("tortilla", pieces,4F, perfectGuacamole);
        perfectGuacamole.getCategories().add(mexican);
        perfectGuacamole.getCategories().add(fastFood);

        recipeRepository.save(perfectGuacamole);
    }

    private void createSpicyChickenTacosRecipe(
            UnitOfMeasure ripe,
            UnitOfMeasure pounds,
            UnitOfMeasure teaspoon,
            UnitOfMeasure tablespoon,
            UnitOfMeasure pieces,
            Category mexican,
            Category fastFood,
            Category american
    ) {
        Notes chickenTacosNotes = new Notes();
        chickenTacosNotes.setRecipeNotes(
                "Look for ancho chile powder with the Mexican ingredients at your grocery store, " +
                "on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, " +
                "the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the " +
                "flavor won't be quite the same.)"
        );

        Recipe chickenTacos = new Recipe();

        chickenTacos.setDescription("Spicy Grilled Chicken Tacos");
        chickenTacos.setPrepTime(20);
        chickenTacos.setCookTime(15);
        chickenTacos.setServings(6);
        chickenTacos.setSource("simplyrecipes.com");
        chickenTacos.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        chickenTacos.setDirections(
                """
                1. Prepare a gas or charcoal grill for medium-high, direct heat.
                2. Make the marinade and coat the chicken.
                3. Grill the chicken.
                4. Warm the tortillas.
                5. Assemble the tacos.
                """
        );
        chickenTacos.setDifficulty(Difficulty.MODERATE);
        // First the ingredients...
        addIngredientToRecipe("chili pepper", tablespoon,2F, chickenTacos);
        addIngredientToRecipe("oregano", teaspoon,1F, chickenTacos);
        addIngredientToRecipe("cumin", teaspoon,1F, chickenTacos);
        addIngredientToRecipe("sugar", teaspoon,4F, chickenTacos);
        addIngredientToRecipe("salt", teaspoon,2F, chickenTacos);
        addIngredientToRecipe("garlic", pieces,1F, chickenTacos);
        addIngredientToRecipe("zest", tablespoon,0.5F, chickenTacos);
        addIngredientToRecipe("orange juice", tablespoon,4F, chickenTacos);
        addIngredientToRecipe("olive oil", tablespoon,4F, chickenTacos);
        addIngredientToRecipe("chicken thighs", pounds,4F, chickenTacos);
        addIngredientToRecipe("corn tortillas", pieces,8F, chickenTacos);
        addIngredientToRecipe("avocado", ripe,2F, chickenTacos);
        chickenTacos.setNotes(chickenTacosNotes);
        chickenTacosNotes.setRecipe(chickenTacos);
        chickenTacos.getCategories().add(mexican);
        chickenTacos.getCategories().add(fastFood);
        chickenTacos.getCategories().add(american);

        recipeRepository.save(chickenTacos);
    }

    private void addIngredientToRecipe(String ingredientName, UnitOfMeasure unitOfMeasure, Float amount, Recipe recipe) {
        Ingredient ingredient = createIngredient(ingredientName, unitOfMeasure, amount, recipe);
        recipe.getIngredients().add(ingredient);
    }

    private UnitOfMeasure getUnitOfMeasureOrCreateIfNotExists(String uom) {
        Optional<UnitOfMeasure> query = unitOfMeasureRepository.findByUom(uom);
        if (query.isPresent()) {
            return query.get();
        }
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom(uom);
        return unitOfMeasureRepository.save(unitOfMeasure);
    }

    private Category getCategoryOrCreateIfNotExists(String categoryName) {
        Optional<Category> query = categoryRepository.findByDescription(categoryName);
        if (query.isPresent()) {
            return query.get();
        }
        Category category = new Category();
        category.setDescription(categoryName);
        return categoryRepository.save(category);
    }

    private Ingredient createIngredient(String name, UnitOfMeasure uom, Float amount, Recipe recipe) {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(name);
        ingredient.setUom(uom);
        ingredient.setAmount(BigDecimal.valueOf(amount));
        ingredient.setRecipe(recipe);
        return ingredient;
    }
}
