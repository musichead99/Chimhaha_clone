package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.utils.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImagesService {

    private final ImagesRepository imagesRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public List<Long> save(Posts post, List<MultipartFile> originals) {
        List<Long> uploadedImagesId = new ArrayList<>();

        for(MultipartFile original : originals) {
            File uploadedFile = fileUploadService.upload(original);

            Images image = imagesRepository.save(Images.builder()
                    .post(post)
                    .realFileName(original.getOriginalFilename())
                    .storedFileName(uploadedFile.getName())
                    .storedFileSize((int) original.getSize())
                    .storedFilePath(uploadedFile.getAbsolutePath())
                    .build());
            uploadedImagesId.add(image.getId());
        }

        return uploadedImagesId;
    }

    @Transactional(readOnly = true)
    public String findImagePathById(Long id) {
        Images image = imagesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 파일을 찾을 수 없습니다."));

        return image.getStoredFilePath();
    }
}
