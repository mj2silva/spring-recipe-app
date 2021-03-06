package dev.manuelsilva.recipeapp.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    @OneToOne
    private UnitOfMeasure uom;
    @ManyToOne(fetch = FetchType.EAGER)
    private Recipe recipe;

    public Ingredient(String description, Float amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = BigDecimal.valueOf(amount);
        this.uom = uom;
    }

}
