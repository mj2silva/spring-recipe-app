package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.converters.RecipeCommandToRecipe;
import dev.manuelsilva.recipeapp.converters.RecipeToRecipeCommand;
import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll().collectList().block();
    }

    @Override
    public Recipe getRecipeById(String id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id).blockOptional();
        if (optionalRecipe.isEmpty()) throw new NotFoundException("Recipe not found");
        return optionalRecipe.get();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipe) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipe);
        if (detachedRecipe == null) return null;
        Recipe savedRecipe = recipeRepository.save(detachedRecipe).block();
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public RecipeCommand getRecipeCommandById(String id) {
        Optional<Recipe> detachedRecipe = recipeRepository.findById(id).blockOptional();
        if (detachedRecipe.isEmpty()) throw new NotFoundException("Recipe not found");
        return recipeToRecipeCommand.convert(detachedRecipe.get());
    }

    @Override
    public void deleteById(String id) {
        recipeRepository.deleteById(id).block();
    }
}
