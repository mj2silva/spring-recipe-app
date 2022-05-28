package dev.manuelsilva.recipeapp.services;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Void> saveImageFile(String id, FilePart file);
}
