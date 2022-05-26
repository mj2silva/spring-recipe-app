package dev.manuelsilva.recipeapp.repositories.reactive;

import dev.manuelsilva.recipeapp.domain.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CategoryReactiveRepositoryTest {
    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Test
    void saveCategoryAndFindAll() {
        Long count = categoryReactiveRepository.count().block();
        Category forProgrammers = new Category();
        forProgrammers.setDescription("for programmers");
        categoryReactiveRepository.save(forProgrammers).block();

        Flux<Category> categoryFlux = categoryReactiveRepository.findAll();
        List<Category> categories = new ArrayList<>();
        categoryFlux.all(categories::add).block();

        assertNotNull(categoryFlux);
        assertNotNull(count);
        assertEquals(count  + 1L, categories.size());
    }
}