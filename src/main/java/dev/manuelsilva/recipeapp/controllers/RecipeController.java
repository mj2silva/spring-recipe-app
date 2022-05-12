package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"/", ""})
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes/index";
    }

    @RequestMapping(value = {"/{recipeId}"})
    public String getRecipe(Model model, @PathVariable String recipeId) {
        Long lRecipeId = Long.valueOf(recipeId);
        Recipe recipe = recipeService.getRecipeById(lRecipeId);
        model.addAttribute("recipe", recipe);
        return "recipes/show";
    }
}
