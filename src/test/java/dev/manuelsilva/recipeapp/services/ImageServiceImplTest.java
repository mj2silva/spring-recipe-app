package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {
    @Mock
    RecipeReactiveRepository recipeRepository;
    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void testSaveImageFile() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");
        FilePart filePart = mock(FilePart.class);
        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.read(
                new ByteArrayResource("some string".getBytes()),new DefaultDataBufferFactory(),1024
        );
        when(filePart.filename()).thenReturn("image.jpg");
        when(filePart.content()).thenReturn(dataBufferFlux);
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        when(recipeRepository.findById(eq("1L"))).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(any())).thenReturn(Mono.just(recipe));

        Mono<Void> imageSaver = imageService.saveImageFile("1L", filePart);
        StepVerifier.create(imageSaver)
                        .expectComplete()
                        .verify();

        verify(recipeRepository, times(1)).save(recipeArgumentCaptor.capture());
        Recipe savedRecipe = recipeArgumentCaptor.getValue();
        assertEquals("some string".getBytes().length, savedRecipe.getImage().length);
    }
}