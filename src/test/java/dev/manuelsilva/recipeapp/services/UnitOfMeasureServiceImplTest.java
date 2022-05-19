package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.domain.UnitOfMeasure;
import dev.manuelsilva.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    UnitOfMeasureService unitOfMeasureService;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository,unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void getAllUnitsOfMeasure() {
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure uom_1 = new UnitOfMeasure();
        uom_1.setId(1L);
        unitOfMeasures.add(uom_1);
        UnitOfMeasure uom_2 = new UnitOfMeasure();
        uom_2.setId(2L);
        unitOfMeasures.add(uom_2);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        Set<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.getAllUnitsOfMeasure();
        assertEquals(2, unitOfMeasureCommands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}