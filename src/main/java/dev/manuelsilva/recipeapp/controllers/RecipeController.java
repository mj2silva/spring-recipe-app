package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
