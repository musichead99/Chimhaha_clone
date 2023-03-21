package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.PostsFindBySubjectResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto dto) {

        Posts posts = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .subject(dto.getSubject())
                .popularFlag(dto.getPopularFlag())
                .build();

        return postsRepository.save(posts).getId();
    }

    @Transactional(readOnly = true)
    public List<PostsFindBySubjectResponseDto> findBySubject(String subject) {
        List<Posts> postsBySubject = postsRepository.findBySubject(subject);
        List<PostsFindBySubjectResponseDto> responses = new ArrayList<>();

        for(Posts post : postsBySubject) {
            responses.add(new PostsFindBySubjectResponseDto(post));
        }
        return responses;
    }

    public PostsFindByIdResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 해당 게시글이 존재하지 않습니다."));

        return new PostsFindByIdResponseDto(posts);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        Posts posts = postsRepository.findById(id).get();
        posts.increaseViewCount();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "해당 게시글이 존재하지 않습니다."));

        posts.update(dto.getTitle(), dto.getContent(), dto.getSubject(), dto.getPopularFlag());

        return posts.getId();
    }

    @Transactional
    public void delete(Long id) {
        postsRepository.deleteById(id);
    }

}
