package dev.manuelsilva.recipeapp.repositories.reactive;

import dev.manuelsilva.recipeapp.bootstrap.PersistenceDataLoader;
import dev.manuelsilva.recipeapp.domain.UnitOfMeasure;
import dev.manuelsilva.recipeapp.repositories.CategoryRepository;
import dev.manuelsilva.recipeapp.repositories.RecipeRepository;
import dev.manuelsilva.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureReactiveRepositoryTest {
    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Test
    void saveUnitOfMeasureAndFindAll() {
        Long count = unitOfMeasureReactiveRepository.count().block();
        UnitOfMeasure kilos = new UnitOfMeasure();
        kilos.setUom("kilos");
        unitOfMeasureReactiveRepository.save(kilos).block();

        Flux<UnitOfMeasure> unitOfMeasureFlux = unitOfMeasureReactiveRepository.findAll();
        List<UnitOfMeasure> unitsOfMeasure = new ArrayList<>();
        unitOfMeasureFlux.all(unitsOfMeasure::add).block();

        assertNotNull(unitOfMeasureFlux);
        assertNotNull(count);
        assertEquals(count  + 1L, unitsOfMeasure.size());
    }
}