package dev.manuelsilva.recipeapp.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {
    @Override
    public void saveImageFile(Long id, MultipartFile file) {
        log.info("Saving file for recipe with id: " + id);
    }
}
