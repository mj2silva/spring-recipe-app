package dev.manuelsilva.recipeapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {
    private String id;
    private String description;
    private BigDecimal amount;
    @DBRef
    private UnitOfMeasure unitOfMeasure;

    public Ingredient(String description, Float amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = BigDecimal.valueOf(amount);
        this.unitOfMeasure = uom;
    }

}
