package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.utils.FileUploadUtils;
import net.chimhaha.clone.dto.images.ImagesSaveResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImagesService {

    private final ImagesRepository imagesRepository;
    private final FileUploadUtils fileUploadUtils;

    @Transactional
    public List<ImagesSaveResponseDto> save(List<MultipartFile> images) {
        return images.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImagesSaveResponseDto save(MultipartFile image) {
        File file = fileUploadUtils.save(image);

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
        Images image = this.findById(id);

        return image.getStoredFilePath();
    }

    @Transactional
    public void delete(Long id) {
        Images image = this.findById(id);

        fileUploadUtils.delete(new File(image.getStoredFilePath()));
        imagesRepository.delete(image);
    }

    @Transactional(readOnly = true) // 스케쥴러에서 사용
    public List<Images> findByPostIsNull() {
        return imagesRepository.findByPostIsNull();
    }

    /* 서비스 계층 내에서만 사용할 메소드들 */

    @Transactional(readOnly = true)
    public List<Images> findByPost(Posts post) {
        return imagesRepository.findByPost(post);
    }

    // 게시글 수정 시 첨부할 수 있는 이미지들만 조회한다. ex) 다른 게시글과 연관관계를 가지지 않거나 parameter로 받은 post와만 연관관계를 가져야 함
    @Transactional(readOnly = true)
    public List<Images> findByIdIn(List<Long> imageIdList, Posts post) {
        List<Images> images = imagesRepository.findByIdAndPostIsNullOrPostIn(imageIdList, post);

        if(images.size() != imageIdList.size()) {
            throw new CustomException(ErrorCode.IMAGES_NOT_ATTACHED);
        }

        return images;
    }

    // 게시글에 첨부할 수 있는 이미지들만 조회한다. ex) 다른 게시글과 연관관계를 가지지 않아야 함
    @Transactional(readOnly = true)
    public List<Images> findByIdIn(List<Long> imageIdList) {
        List<Images> images = imagesRepository.findByIdAndPostIsNullIn(imageIdList);

        if(images.size() != imageIdList.size()) {
            throw new CustomException(ErrorCode.IMAGES_NOT_ATTACHED);
        }

        return images;
    }

    @Transactional(readOnly = true)
    public Images findById(Long id) {
        return imagesRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGES_NOT_FOUND));
    }
}
