package dev.manuelsilva.recipeapp.controllers;

import dev.manuelsilva.recipeapp.services.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest {
    @Mock
    ImageService imageService;
    ImageController imageController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageController = new ImageController(imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void testHandleImagePost() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "testing.txt",
                "text/plain",
                "Some random text".getBytes()
        );
        mockMvc.perform(multipart("/recipes/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/recipes/1"));

        verify(imageService, times(1)).saveImageFile(eq(1L), any(MultipartFile.class));
    }
}