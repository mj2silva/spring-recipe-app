package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;

import java.util.List;
import java.util.Set;

public interface UnitOfMeasureService {
    List<UnitOfMeasureCommand> getAllUnitsOfMeasure();
}
