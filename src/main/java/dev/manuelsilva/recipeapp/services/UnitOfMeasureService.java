package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;

import java.util.List;

public interface UnitOfMeasureService {
    List<UnitOfMeasureCommand> getAllUnitsOfMeasure();
}
