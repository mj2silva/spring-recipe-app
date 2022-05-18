package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.services.IngredientService;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @RequestMapping("/recipes/{recipeId}/ingredients")
    public String getIngredientsList(Model model, @PathVariable String recipeId) {
        RecipeCommand command = recipeService.getRecipeCommandById(Long.valueOf(recipeId));
        model.addAttribute("recipe", command);
        return "recipes/ingredients/list";
    }

    @RequestMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    public String showIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        IngredientCommand ingredient = ingredientService.findById(Long.valueOf(ingredientId));
        model.addAttribute("ingredient", ingredient);
        return "recipes/ingredients/show";
    }
}
