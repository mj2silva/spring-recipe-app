package dev.manuelsilva.recipeapp.services;

import dev.manuelsilva.recipeapp.domain.Recipe;
import dev.manuelsilva.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {
    @Mock
    RecipeRepository recipeRepository;
    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void testSaveImageFile() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile(
                "image-file",
                "test-file.txt",
                "text/plain",
                "Some random and unusual text".getBytes()
        );
        Recipe recipe = new Recipe();
        recipe.setId("1L");
        Optional<Recipe> optionalRecipe = Optional.of(recipe);
        when(recipeRepository.findById(eq("1L"))).thenReturn(optionalRecipe);
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        imageService.saveImageFile("1L", multipartFile);

        verify(recipeRepository, times(1)).save(recipeArgumentCaptor.capture());
        Recipe savedRecipe = recipeArgumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}