package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.IngredientCommand;
import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.commands.UnitOfMeasureCommand;
import dev.manuelsilva.recipeapp.services.IngredientService;
import dev.manuelsilva.recipeapp.services.RecipeService;
import dev.manuelsilva.recipeapp.services.UnitOfMeasureService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
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
        Mono<RecipeCommand> command = recipeService.getRecipeCommandById(recipeId);
        model.addAttribute("recipe", command);
        return "recipes/ingredients/list";
    }
    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    public String showIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        Mono<IngredientCommand> ingredient = ingredientService.findById(recipeId, ingredientId);
        model.addAttribute("ingredient", ingredient);
        return "recipes/ingredients/show";
    }

    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}/edit")
    public String editIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        Mono<IngredientCommand> ingredient = ingredientService.findById(recipeId, ingredientId);
        Flux<UnitOfMeasureCommand> unitsOfMeasure = unitOfMeasureService.getAllUnitsOfMeasure();
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("unitsOfMeasure", unitsOfMeasure);
        return "recipes/ingredients/edit";
    }

    @GetMapping("/recipes/{recipeId}/ingredients/new")
    public String createIngredient(Model model, @PathVariable String recipeId) {
        Mono<RecipeCommand> recipe = recipeService.getRecipeCommandById(recipeId);
        if (recipe == null) return "redirect:/recipes";
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        Flux<UnitOfMeasureCommand> unitsOfMeasure = unitOfMeasureService.getAllUnitsOfMeasure();
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("unitsOfMeasure", unitsOfMeasure);
        return "recipes/ingredients/edit";
    }

    @GetMapping("/recipes/{recipeId}/ingredients/{ingredientId}/delete")
    public Mono<String> deleteIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
        return ingredientService.deleteById(recipeId, ingredientId).thenReturn(String.format("redirect:/recipes/%s/ingredients", recipeId));
    }

    @PostMapping("/recipes/{recipeId}/ingredients")
    public Mono<String> saveOrUpdateIngredient(@Valid @ModelAttribute Mono<IngredientCommand> ingredientCommand, @PathVariable String recipeId) {
        return ingredientCommand
                .flatMap(command -> ingredientService.save(recipeId, command))
                .map(savedCommand -> String.format("redirect:/recipes/%s/ingredients/%s", recipeId, savedCommand.getId()))
                .doOnError(thr -> log.error(thr.getMessage()))
                .onErrorResume(throwable -> Mono.just(String.format("redirect:/recipes/%s/ingredients/new", recipeId)));
    }
}
