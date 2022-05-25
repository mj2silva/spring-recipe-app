package dev.manuelsilva.recipeapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {
    private String id;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;
    private Recipe recipe;

    public Ingredient(String description, Float amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = BigDecimal.valueOf(amount);
        this.uom = uom;
    }

}
