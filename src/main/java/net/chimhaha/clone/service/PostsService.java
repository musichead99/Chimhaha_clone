package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.PostsFindResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final BoardsRepository boardsRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public Long save(PostsSaveRequestDto dto) {

        Boards board = boardsRepository.getReferenceById(dto.getBoardId());
        Category category = categoryRepository.getReferenceById(dto.getCategoryId());
        Menu menu = menuRepository.getReferenceById(dto.getMenuId());

        Posts posts = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .popularFlag(dto.getPopularFlag())
                .board(board)
                .build();

        return postsRepository.save(posts).getId();
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> find(Pageable pageable) {
        Page<Posts> posts = postsRepository.findAll(pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.getReferenceById(categoryId);

        Page<Posts> posts = postsRepository.findByCategory(category, pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findByBoard(Long boardId, Pageable pageable) {
        Boards boards = boardsRepository.getReferenceById(boardId);

        Page<Posts> posts = postsRepository.findByBoard(boards, pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public PostsFindByIdResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 해당 게시글이 존재하지 않습니다."));

        return PostsFindByIdResponseDto.from(post);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 해당 게시글이 존재하지 않습니다."));
        post.increaseViewCount();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "해당 게시글이 존재하지 않습니다."));

        Category category = categoryRepository.getReferenceById(dto.getCategoryId());

        posts.update(dto.getTitle(), dto.getContent(), category, dto.getPopularFlag());

        return posts.getId();
    }

    @Transactional
    public void delete(Long id) {
        postsRepository.deleteById(id);
    }
}
