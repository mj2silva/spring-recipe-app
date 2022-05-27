package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> getAllUnitsOfMeasure() {
        return unitOfMeasureRepository
                .findAll()
                .mapNotNull(unitOfMeasureToUnitOfMeasureCommand::convert);
    }
}
