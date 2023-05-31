package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.utils.FileUploadService;
import net.chimhaha.clone.web.dto.images.ImagesSaveResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImagesService {

    private final ImagesRepository imagesRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public List<ImagesSaveResponseDto> save(List<MultipartFile> images) {
        return images.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImagesSaveResponseDto save(MultipartFile image) {
        File file = fileUploadService.upload(image);

        Images uploadedImage = imagesRepository.save(Images.builder()
                .realFileName(file.getName().substring(37))
                .storedFileName(file.getName())
                .storedFileSize((int)file.length())
                .storedFilePath(file.getAbsolutePath())
                .build());

        return ImagesSaveResponseDto.from(uploadedImage);
    }

    @Transactional(readOnly = true)
    public String findImagePathById(Long id) {
        Images image = imagesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 파일을 찾을 수 없습니다."));

        return image.getStoredFilePath();
    }

    /* 서비스 계층 내에서만 사용할 메소드들 */

    @Transactional(readOnly = true)
    List<Images> findByPost(Posts post) {
        return imagesRepository.findByPost(post);
    }

    @Transactional(readOnly = true)
    List<Images> findByIdIn(List<Long> imageIdList) {
        return imagesRepository.findByIdIn(imageIdList);
    }
}
