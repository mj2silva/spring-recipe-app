package dev.manuelsilva.recipeapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private Long id;
    private Long recipeId;
    private String description;
    private UnitOfMeasureCommand unitOfMeasure;
    private BigDecimal amount;
    private RecipeCommand recipe;
}
