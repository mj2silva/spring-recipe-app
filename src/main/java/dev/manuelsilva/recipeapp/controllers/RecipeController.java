package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"/", ""})
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes/index";
    }

    @GetMapping(value = {"/{recipeId}"})
    public String getRecipe(Model model, @PathVariable String recipeId) {
        Mono<RecipeCommand> recipe = recipeService.getRecipeCommandById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipes/show";
    }

    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipes/form";
    }

    @GetMapping("/{recipeId}/update")
    public String editRecipe(Model model, @PathVariable String recipeId) {
        Mono<RecipeCommand> recipeCommand = recipeService.getRecipeCommandById(recipeId);
        model.addAttribute("recipe", recipeCommand);
        return "recipes/form";
    }

    @GetMapping("/{recipeId}/delete")
    public String deleteRecipe(@PathVariable String recipeId) {
        recipeService.deleteById(recipeId);
        return "redirect:/recipes/";
    }

    @PostMapping({"", "/"})
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "recipes/form";
        }
        Mono<RecipeCommand> savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipes/" + savedCommand.block().getId();
    }

    @GetMapping("/{recipeId}/change-image")
    public String changeImage(Model model, @PathVariable String recipeId) {
        Mono<RecipeCommand> recipe = recipeService.getRecipeCommandById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipes/imageForm";
    }

    /*@GetMapping("/{recipeId}/image")
    public void retrieveImage(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipe = recipeService.getRecipeCommandById(recipeId);
        // TODO: Check if the recipe actually has an image
        byte[] bytesFromImage = new byte[recipe.getImage().length];
        for (int i = 0; i < recipe.getImage().length; i++) {
            bytesFromImage[i] = recipe.getImage()[i];
        }
        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(bytesFromImage);
        IOUtils.copy(inputStream, response.getOutputStream());
    }*/
}
