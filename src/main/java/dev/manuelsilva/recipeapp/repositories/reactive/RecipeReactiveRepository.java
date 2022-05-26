package dev.manuelsilva.recipeapp.repositories.reactive;

import dev.manuelsilva.recipeapp.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
