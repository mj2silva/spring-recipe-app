package dev.manuelsilva.recipeapp.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UnitOfMeasureCommand {
    private Long id;
    private String uom;
}
