package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.ImagesService;
import net.chimhaha.clone.utils.FileUploadService;
import net.chimhaha.clone.utils.ImageFileDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ImagesController {

    private final ImagesService imagesService;
    private final FileUploadService fileUploadService;

    @GetMapping(value = "/images/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        ImageFileDto dto = fileUploadService.getImageFile(imagesService.findImagePathById(id));

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, dto.getMediaType().toString())
                .body(dto.getBytes());
    }
}
