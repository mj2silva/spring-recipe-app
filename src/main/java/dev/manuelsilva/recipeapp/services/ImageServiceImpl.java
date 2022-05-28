package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.exceptions.NotFoundException;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
    public Mono<Void> saveImageFile(String id, FilePart file) {
        // TODO: Check if we can change Byte[] for byte[]
             return file.content()
                 .mapNotNull(DataBuffer::asInputStream)
                 .map(inputStream -> {
                     try {
                         var bytesArr = inputStream.readAllBytes();
                         Byte[] bytes = new Byte[bytesArr.length];
                         for (int i = 0; i < bytes.length; i++) {
                             bytes[i] = bytesArr[i];
                         }
                         return bytes;
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                     })
                     .flatMap(bytes -> {
                         return recipeRepository.findById(id).mapNotNull(recipe1 -> {
                             recipe1.setImage(bytes);
                             return recipe1;
                         });
                     })
                     .flatMap(recipeRepository::save)
                .then(Mono.empty());
             };
            /*Mono<Recipe> recipeMono = recipeRepository
                    .findById(id)
                    .switchIfEmpty(
                            Mono.error(new NotFoundException("Recipe not found"))
                    );
            return recipeMono.mapNotNull(recipe -> {
                try {
                    Byte[] bytes = new Byte[file.getBytes().length];
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = file.getBytes()[i];
                    }
                    recipe.setImage(bytes);
                    return recipe;
                } catch (IOException ex) {
                    log.error("Some error occurred during file saving", ex);
                    ex.printStackTrace();
                    return recipe;
                }
            })
                    .flatMap(recipeRepository::save)
                    .then(Mono.empty());*/
}
