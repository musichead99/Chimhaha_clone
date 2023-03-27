package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.PostsFindResponseDto;
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
    private final BoardsRepository boardsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto dto) {

        Boards board = boardsRepository.getReferenceById(dto.getBoardId());

        Posts posts = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .subject(dto.getSubject())
                .popularFlag(dto.getPopularFlag())
                .board(board)
                .build();

        return postsRepository.save(posts).getId();
    }

    @Transactional(readOnly = true)
    public List<PostsFindResponseDto> find() {
        List<Posts> posts = postsRepository.findAll();

        return makeEntityToDto(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsFindResponseDto> findBySubject(String subject) {
        List<Posts> posts = postsRepository.findBySubject(subject);

        return makeEntityToDto(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsFindResponseDto> findByBoard(String categoryName) {
        Boards boards = boardsRepository.getReferenceByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException(categoryName + " 카테고리가 존재하지 않습니다"));

        List<Posts> posts = postsRepository.findByBoard(boards);

        return makeEntityToDto(posts);
    }

    @Transactional(readOnly = true)
    public PostsFindByIdResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 해당 게시글이 존재하지 않습니다."));

        return new PostsFindByIdResponseDto(post);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        Posts post = postsRepository.findById(id).get();
        post.increaseViewCount();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "해당 게시글이 존재하지 않습니다."));

        posts.update(dto);

        return posts.getId();
    }

    @Transactional
    public void delete(Long id) {
        postsRepository.deleteById(id);
    }

    private List<PostsFindResponseDto> makeEntityToDto(List<Posts> posts) {
        List<PostsFindResponseDto> responses = new ArrayList<>();

        for(Posts post : posts) {
            responses.add(new PostsFindResponseDto(post));
        }
        return responses;
    }
}
