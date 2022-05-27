package dev.manuelsilva.recipeapp.repositories;

import dev.manuelsilva.recipeapp.bootstrap.PersistenceDataLoader;
import dev.manuelsilva.recipeapp.domain.UnitOfMeasure;
import dev.manuelsilva.recipeapp.repositories.reactive.CategoryReactiveRepository;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import dev.manuelsilva.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    @Autowired
    CategoryReactiveRepository categoryRepository;
    @Autowired
    RecipeReactiveRepository recipeRepository;

    @BeforeEach
    void setUp() throws Exception {
        PersistenceDataLoader dataLoader = new PersistenceDataLoader(recipeRepository, categoryRepository, unitOfMeasureRepository);
        dataLoader.run();
    }

    @Test
    void findByUom() {
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByUom("Teaspoon").blockOptional();
        UnitOfMeasure teaspoon = unitOfMeasure.orElse(null);

        assertNotNull(teaspoon);
        assertEquals("Teaspoon", teaspoon.getUom());
    }
}