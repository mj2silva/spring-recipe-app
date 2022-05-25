package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.services.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/recipes/{recipeId}/image")
    public String handlePostImage(@PathVariable String recipeId, @RequestParam("image") MultipartFile imageFile) {
        imageService.saveImageFile(recipeId, imageFile);
        return String.format("redirect:/recipes/%s", recipeId);
    }
}
