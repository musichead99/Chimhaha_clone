package net.chimhaha.clone.controller;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.config.annotation.MultipartExist;
import net.chimhaha.clone.service.ImagesService;
import net.chimhaha.clone.utils.FileUploadUtils;
import net.chimhaha.clone.utils.ImageFileDto;
import net.chimhaha.clone.dto.images.ImagesSaveResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
public class ImagesController {

    private final ImagesService imagesService;
    private final FileUploadUtils fileUploadUtils;

    @PostMapping(value = "/images")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<ImagesSaveResponseDto> save(@MultipartExist @RequestPart("images") List<MultipartFile> images) {
        return imagesService.save(images);
    }

    @GetMapping(value = "/images/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        ImageFileDto dto = fileUploadUtils.getImageFile(imagesService.findImagePathById(id));

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, dto.getMediaType().toString())
                .body(dto.getBytes());
    }

    @DeleteMapping(value = "/images/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        imagesService.delete(id);
    }
}
