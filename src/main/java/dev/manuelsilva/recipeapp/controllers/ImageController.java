package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.services.ImageService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Controller
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/recipes/{recipeId}/image")
    public Mono<String> handlePostImage(@PathVariable String recipeId, @RequestPart("image") Mono<FilePart> imageFile) {
        return imageFile
                .flatMap(filePart -> imageService.saveImageFile(recipeId, filePart))
                .then(Mono.just(String.format("redirect:/recipes/%s", recipeId)));
    }
}
