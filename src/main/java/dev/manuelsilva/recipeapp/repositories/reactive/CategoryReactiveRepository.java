package dev.manuelsilva.recipeapp.repositories.reactive;

import dev.manuelsilva.recipeapp.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {
}
