package dev.manuelsilva.recipeapp.repositories;

import dev.manuelsilva.recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
