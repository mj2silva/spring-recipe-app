package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {
    private final RecipeReactiveRepository recipeRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String id, MultipartFile file) {
        // TODO: Check if we can change Byte[] for byte[]
        try {
            Recipe recipe = recipeRepository.findById(id).block();
            if (recipe == null) throw new RuntimeException("Recipe not found");
            Byte[] bytes = new Byte[file.getBytes().length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = file.getBytes()[i];
            }
            recipe.setImage(bytes);
            recipeRepository.save(recipe).block();
        } catch (IOException ex) {
            log.error("Some error occurred during file saving", ex);
            ex.printStackTrace();
        }
        return Mono.empty();
    }
}
