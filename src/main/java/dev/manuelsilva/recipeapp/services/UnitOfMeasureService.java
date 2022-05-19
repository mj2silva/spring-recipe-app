package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> getAllUnitsOfMeasure();
}
