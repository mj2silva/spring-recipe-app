package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.commands.RecipeCommand;
import dev.manuelsilva.recipeapp.services.RecipeService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller
@Log4j2
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
    public Mono<String> saveOrUpdate(@Valid @ModelAttribute("recipe") Mono<RecipeCommand> command) {
        return command
                .flatMap(recipeService::saveRecipeCommand)
                .map(recipe -> "redirect:/recipes/" + recipe.getId())
                .doOnError(thr -> log.error(thr.getMessage()))
                .onErrorResume(throwable -> Mono.just("recipes/form"));
    }

    @GetMapping("/{recipeId}/change-image")
    public String changeImage(Model model, @PathVariable String recipeId) {
        Mono<RecipeCommand> recipe = recipeService.getRecipeCommandById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipes/imageForm";
    }

    @GetMapping(value = "/{recipeId}/image", produces = {MediaType.IMAGE_JPEG_VALUE})
    public Mono<Void> retrieveImage(@PathVariable String recipeId, ServerWebExchange exchange) {
        Mono<RecipeCommand> recipeCommandMono = recipeService.getRecipeCommandById(recipeId);
        // TODO: Check if the recipe actually has an image
        return recipeCommandMono.flatMap(recipe -> {
            byte[] bytesFromImage = new byte[recipe.getImage().length];
            for (int i = 0; i < recipe.getImage().length; i++) {
                bytesFromImage[i] = recipe.getImage()[i];
            }
            // InputStream inputStream = new ByteArrayInputStream(bytesFromImage);
            DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(bytesFromImage);
            return Mono.just(dataBuffer);
        }).flatMap(stream -> exchange.getResponse().writeWith(Mono.just(stream)));

    }
}
