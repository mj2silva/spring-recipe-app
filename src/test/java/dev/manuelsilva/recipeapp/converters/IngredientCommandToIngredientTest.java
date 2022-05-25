package dev.manuelsilva.recipeapp.converters;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.domain.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    static final BigDecimal AMOUNT = new BigDecimal(10);
    static final String DESCRIPTION = "Meatballs";
    static final String INGREDIENT_ID = "1L";
    static final String UOM_ID = "1L";

    IngredientCommandToIngredient converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        // Given
        IngredientCommand command = new IngredientCommand();
        command.setId(INGREDIENT_ID);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);
        UnitOfMeasureCommand unitOfMeasure = new UnitOfMeasureCommand();
        unitOfMeasure.setId(UOM_ID);
        command.setUnitOfMeasure(unitOfMeasure);

        // When
        Ingredient ingredient = converter.convert(command);

        // Then
        assertNotNull(ingredient);
        assertNotNull(ingredient.getUom());
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(UOM_ID, ingredient.getUom().getId());
    }

    @Test
    void convertWithNullUom() {
        // Given
        IngredientCommand command = new IngredientCommand();
        command.setId(INGREDIENT_ID);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);

        // When
        Ingredient ingredient = converter.convert(command);

        // Then
        assertNotNull(ingredient);
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertNull(ingredient.getUom());
    }
}