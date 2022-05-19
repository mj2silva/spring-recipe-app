package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.services.IngredientService;
import dev.manuelsilva.recipeapp.services.RecipeService;
import dev.manuelsilva.recipeapp.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.Set;

@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
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
        if (!ingredient.getRecipeId().equals(Long.valueOf(recipeId))) {
            return String.format("redirect:/recipes/%s/ingredients/%s", ingredient.getRecipeId(), ingredient.getId());
        }
        model.addAttribute("ingredient", ingredient);
        return "recipes/ingredients/show";
    }

    @RequestMapping("/recipes/{recipeId}/ingredients/{ingredientId}/edit")
    public String editIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        IngredientCommand ingredient = ingredientService.findById(Long.valueOf(ingredientId));
        if (!ingredient.getRecipeId().equals(Long.valueOf(recipeId))) {
            return String.format("redirect:/recipes/%s/ingredients/%s/edit", ingredient.getRecipeId(), ingredient.getId());
        }
        Set<UnitOfMeasureCommand> unitsOfMeasure = unitOfMeasureService.getAllUnitsOfMeasure();
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("unitsOfMeasure", unitsOfMeasure);
        return "recipes/ingredients/edit";
    }

    @PostMapping("/recipes/{recipeId}/ingredients")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand savedIngredient = ingredientService.save(ingredientCommand);
        return String.format("redirect:/recipes/%s/ingredients/%s", savedIngredient.getRecipeId(), savedIngredient.getId());
    }

}
