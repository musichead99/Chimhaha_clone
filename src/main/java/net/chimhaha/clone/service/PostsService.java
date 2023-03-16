package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.PostsFindByCategoryResponseDto;
import net.chimhaha.clone.web.dto.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.PostsSaveRequestDto;
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
                .category(dto.getCategory())
                .popularFlag(dto.getPopularFlag())
                .build();

        return postsRepository.save(posts).getId();
    }

    public List<PostsFindByCategoryResponseDto> findByCategory(String category) {
        List<Posts> categorizedPosts = postsRepository.findByCategory(category);
        List<PostsFindByCategoryResponseDto> responses = new ArrayList<>();

        for(Posts categorizedPost : categorizedPosts) {
            responses.add(new PostsFindByCategoryResponseDto(categorizedPost));
        }
        return responses;
    }

    public PostsFindByIdResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " 해당 게시글이 존재하지 않습니다."));

        return new PostsFindByIdResponseDto(posts);
    }
}
