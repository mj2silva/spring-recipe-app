package dev.manuelsilva.recipeapp.repositories.reactive;

import dev.manuelsilva.recipeapp.domain.Difficulty;
import dev.manuelsilva.recipeapp.domain.Recipe;
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
class RecipeReactiveRepositoryTest {
    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Test
    void saveRecipeAndFindAll() {
        Long count = recipeReactiveRepository.count().block();
        Recipe lomitoSaltado = new Recipe();
        lomitoSaltado.setDescription("Peruvian Lomo Saltado");
        lomitoSaltado.setUrl("https://fakelomosaltadorecipeurl.com");
        lomitoSaltado.setSource("Fake recipes");
        lomitoSaltado.setServings(3);
        lomitoSaltado.setPrepTime(15);
        lomitoSaltado.setCookTime(15);
        lomitoSaltado.setDirections("Just do it!");
        lomitoSaltado.setDifficulty(Difficulty.MODERATE);

        recipeReactiveRepository.save(lomitoSaltado).block();

        Flux<Recipe> recipeFlux = recipeReactiveRepository.findAll();
        List<Recipe> recipes = new ArrayList<>();
        recipeFlux.all(recipes::add).block();

        assertNotNull(recipeFlux);
        assertNotNull(count);
        assertEquals(count  + 1L, recipes.size());
    }
}