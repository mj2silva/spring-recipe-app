package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> getAllUnitsOfMeasure();
}
