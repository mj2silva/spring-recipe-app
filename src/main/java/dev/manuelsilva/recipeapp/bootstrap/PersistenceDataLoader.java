package dev.manuelsilva.recipeapp.bootstrap;

import dev.manuelsilva.recipeapp.domain.*;
import dev.manuelsilva.recipeapp.repositories.reactive.CategoryReactiveRepository;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import dev.manuelsilva.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class PersistenceDataLoader implements CommandLineRunner {
    private final RecipeReactiveRepository recipeRepository;
    private final CategoryReactiveRepository categoryRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public PersistenceDataLoader(RecipeReactiveRepository recipeRepository, CategoryReactiveRepository categoryRepository, UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting to load data");
        if (recipeRepository.count().blockOptional().orElse(0L) == 0) {
            loadData();
            log.info("Finish loading data");
        } else {
            log.info("Recipes founded in database, initial population skipped");
        }
    }

    private void loadData() {
        // If there are not already in the database, units of measure are created
        log.info("Creating units of measure");
        UnitOfMeasure ripe = getUnitOfMeasureOrCreateIfNotExists("Ripe");
        UnitOfMeasure teaspoon = getUnitOfMeasureOrCreateIfNotExists("Teaspoon");
        UnitOfMeasure tablespoon = getUnitOfMeasureOrCreateIfNotExists("Tablespoon");
        UnitOfMeasure pieces = getUnitOfMeasureOrCreateIfNotExists("Piece");
        UnitOfMeasure pinch = getUnitOfMeasureOrCreateIfNotExists("Pinch");
        UnitOfMeasure slices = getUnitOfMeasureOrCreateIfNotExists("Slices");
        UnitOfMeasure pounds = getUnitOfMeasureOrCreateIfNotExists("Pounds");

        // Retrieving categories
        log.info("Creating categories");
        Category mexican = getCategoryOrCreateIfNotExists("Mexican");
        Category fastFood = getCategoryOrCreateIfNotExists("Fast food");
        Category american = getCategoryOrCreateIfNotExists("American");

        log.info("Creating recipes");
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
        Recipe perfectGuacamole = new Recipe();

        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes(
                "Be careful handling chilis! If using, it's best to wear food-safe gloves. " +
                        "If no gloves are available, wash your hands thoroughly after handling, and " +
                        "do not touch your eyes or the area near your eyes for several hours afterwards."
        );
        perfectGuacamole.setNotes(guacamoleNotes);
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
        perfectGuacamole.addIngredient(new Ingredient("avocado",2F, ripe));
        perfectGuacamole.addIngredient(new Ingredient("kosher salt",0.25F, teaspoon));
        perfectGuacamole.addIngredient(new Ingredient("lemon juice",1F, tablespoon));
        perfectGuacamole.addIngredient(new Ingredient("chilis",4F, pieces));
        perfectGuacamole.addIngredient(new Ingredient("cilantro",2F, tablespoon));
        perfectGuacamole.addIngredient(new Ingredient("black pepper",1F, pinch));
        perfectGuacamole.addIngredient(new Ingredient("tomato",0.5F, ripe));
        perfectGuacamole.addIngredient(new Ingredient("red radish",4F, slices));
        perfectGuacamole.addIngredient(new Ingredient("tortilla",4F, pieces));
        perfectGuacamole.addCategory(mexican);
        perfectGuacamole.addCategory(fastFood);

        recipeRepository.save(perfectGuacamole).block();
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
        Recipe chickenTacos = new Recipe();

        Notes chickenTacosNotes = new Notes();
        chickenTacosNotes.setRecipeNotes(
                "Look for ancho chile powder with the Mexican ingredients at your grocery store, " +
                        "on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, " +
                        "the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the " +
                        "flavor won't be quite the same.)"
        );
        chickenTacos.setNotes(chickenTacosNotes);
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
        chickenTacos.addIngredient(new Ingredient("chili pepper",2F, tablespoon));
        chickenTacos.addIngredient(new Ingredient("oregano",1F, teaspoon));
        chickenTacos.addIngredient(new Ingredient("cumin",1F, teaspoon));
        chickenTacos.addIngredient(new Ingredient("sugar",4F, teaspoon));
        chickenTacos.addIngredient(new Ingredient("salt",2F, teaspoon));
        chickenTacos.addIngredient(new Ingredient("garlic",1F, pieces));
        chickenTacos.addIngredient(new Ingredient("zest", 5F ,tablespoon));
        chickenTacos.addIngredient(new Ingredient("orange juice",4F, tablespoon));
        chickenTacos.addIngredient(new Ingredient("olive oil",4F, tablespoon));
        chickenTacos.addIngredient(new Ingredient("chicken thighs",4F, pounds));
        chickenTacos.addIngredient(new Ingredient("corn tortillas",8F, pieces));
        chickenTacos.addIngredient(new Ingredient("avocado",2F, ripe));
        chickenTacos.addCategory(mexican);
        chickenTacos.addCategory(fastFood);
        chickenTacos.addCategory(american);

        recipeRepository.save(chickenTacos).block();
    }

    private UnitOfMeasure getUnitOfMeasureOrCreateIfNotExists(String uom) {
        Optional<UnitOfMeasure> query = unitOfMeasureRepository.findByUom(uom).blockOptional();
        if (query.isPresent()) {
            return query.get();
        }
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUom(uom);
        return unitOfMeasureRepository.save(unitOfMeasure).block();
    }

    private Category getCategoryOrCreateIfNotExists(String categoryName) {
        Optional<Category> query = categoryRepository.findByDescription(categoryName).blockOptional();
        if (query.isPresent()) {
            return query.get();
        }
        Category category = new Category();
        category.setDescription(categoryName);
        return categoryRepository.save(category).block();
    }
}
