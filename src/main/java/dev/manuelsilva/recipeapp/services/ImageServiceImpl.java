package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.RecipeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long id, MultipartFile file) {
        // TODO: Check if we can change Byte[] for byte[]
        try {
            Recipe recipe = recipeRepository.findById(id).orElse(null);
            if (recipe == null) throw new RuntimeException("Recipe not found");
            Byte[] bytes = new Byte[file.getBytes().length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = file.getBytes()[i];
            }
            recipe.setImage(bytes);
            recipeRepository.save(recipe);
        } catch (IOException ex) {
            log.error("Some error occurred during file saving", ex);
            ex.printStackTrace();
        }
    }
}
