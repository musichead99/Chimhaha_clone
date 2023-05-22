package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
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

    private final PostsRepository postsRepository;
    private final ImagesRepository imagesRepository;

    @Transactional
    public List<Long> save(Long postId, List<File> files, List<MultipartFile> originals) {
        List<Long> uploadedImagesId = new ArrayList<>();

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 업습니다."));


        for (int i = 0; i < files.size(); i++) {
            Images image = Images.builder()
                    .post(post)
                    .realFileName(originals.get(i).getOriginalFilename())
                    .storedFileName(files.get(i).getName())
                    .storedFileSize(originals.get(i).getSize())
                    .storedFilePath(files.get(i).getAbsolutePath())
                    .build();
            imagesRepository.save(image);
            uploadedImagesId.add(image.getId());
        }

        return uploadedImagesId;
    }
}
