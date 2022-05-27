package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.services.IngredientService;
import dev.manuelsilva.recipeapp.services.RecipeService;
import dev.manuelsilva.recipeapp.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    @GetMapping("/recipes/{recipeId}/ingredients")
    public String getIngredientsList(Model model, @PathVariable String recipeId) {
        RecipeCommand command = recipeService.getRecipeCommandById(recipeId);
        model.addAttribute("recipe", command);
        return "recipes/ingredients/list";
    }
    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    public String showIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        IngredientCommand ingredient = ingredientService.findById(recipeId, ingredientId);
        if (!ingredient.getRecipeId().equals(recipeId)) {
            return String.format("redirect:/recipes/%s/ingredients/%s", ingredient.getRecipeId(), ingredient.getId());
        }
        model.addAttribute("ingredient", ingredient);
        return "recipes/ingredients/show";
    }

    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}/edit")
    public String editIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        IngredientCommand ingredient = ingredientService.findById(recipeId, ingredientId);
        List<UnitOfMeasureCommand> unitsOfMeasure = unitOfMeasureService.getAllUnitsOfMeasure().collectList().block();
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("unitsOfMeasure", unitsOfMeasure);
        return "recipes/ingredients/edit";
    }

    @GetMapping("/recipes/{recipeId}/ingredients/new")
    public String createIngredient(Model model, @PathVariable String recipeId) {
        RecipeCommand recipe = recipeService.getRecipeCommandById(recipeId);
        if (recipe == null) return "redirect:/recipes";
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setRecipeDescription(recipe.getDescription());
        List<UnitOfMeasureCommand> unitsOfMeasure = unitOfMeasureService.getAllUnitsOfMeasure().collectList().block();
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("unitsOfMeasure", unitsOfMeasure);
        return "recipes/ingredients/edit";
    }

    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        ingredientService.findById(recipeId, ingredientId); // In order to throw an exception in case it fails
        ingredientService.deleteById(recipeId, ingredientId);
        return String.format("redirect:/recipes/%s/ingredients", recipeId);
    }

    @PostMapping("/recipes/{recipeId}/ingredients")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand ingredientCommand, @PathVariable String recipeId) {
        IngredientCommand savedIngredient = ingredientService.save(recipeId, ingredientCommand);
        return String.format("redirect:/recipes/%s/ingredients/%s", savedIngredient.getRecipeId(), savedIngredient.getId());
    }
}
