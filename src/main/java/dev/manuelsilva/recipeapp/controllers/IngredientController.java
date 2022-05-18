package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class IngredientController {
    private final RecipeService recipeService;

    public IngredientController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipes/{recipeId}/ingredients")
    public String getIngredientsList(Model model, @PathVariable String recipeId) {
        RecipeCommand command = recipeService.getRecipeCommandById(Long.valueOf(recipeId));
        model.addAttribute("recipe", command);
        return "recipes/ingredients/list";
    }
}
