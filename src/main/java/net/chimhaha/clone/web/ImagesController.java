package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.annotation.MultipartExist;
import net.chimhaha.clone.service.ImagesService;
import net.chimhaha.clone.utils.FileUploadService;
import net.chimhaha.clone.utils.ImageFileDto;
import net.chimhaha.clone.web.dto.images.ImagesSaveResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImagesController {

    private final ImagesService imagesService;
    private final FileUploadService fileUploadService;

    @PostMapping(value = "/images")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<ImagesSaveResponseDto> save(@MultipartExist @RequestPart("images") List<MultipartFile> images) {
        return imagesService.save(images);
    }

    @GetMapping(value = "/images/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        ImageFileDto dto = fileUploadService.getImageFile(imagesService.findImagePathById(id));

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, dto.getMediaType().toString())
                .body(dto.getBytes());
    }
}
