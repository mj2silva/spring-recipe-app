package dev.manuelsilva.recipeapp.repositories.reactive;

import dev.manuelsilva.recipeapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {

}
