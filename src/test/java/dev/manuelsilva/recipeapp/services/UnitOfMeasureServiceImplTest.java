package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.domain.UnitOfMeasure;
import dev.manuelsilva.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    UnitOfMeasureService unitOfMeasureService;
    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository,unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void getAllUnitsOfMeasure() {
        Flux<UnitOfMeasure> unitOfMeasures;
        UnitOfMeasure uom_1 = new UnitOfMeasure();
        uom_1.setId("1L");
        UnitOfMeasure uom_2 = new UnitOfMeasure();
        uom_2.setId("2L");

        unitOfMeasures = Flux.just(uom_1, uom_2);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        Flux<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.getAllUnitsOfMeasure();
        assertEquals(2, unitOfMeasureCommands.count().block());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}