package dev.manuelsilva.recipeapp.repositories;

import dev.manuelsilva.recipeapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
