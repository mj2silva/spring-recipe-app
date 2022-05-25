package dev.manuelsilva.recipeapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;
    private String recipeDescription;
    @NotBlank
    @Size(min = 3, max = 255)
    private String description;
    private UnitOfMeasureCommand unitOfMeasure;
    private BigDecimal amount;
    private RecipeCommand recipe;
}
