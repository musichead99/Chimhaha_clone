package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final BoardsService boardsService;
    private final CategoryService categoryService;
    private final MenuService menuService;
    private final ImagesService imagesService;

    @Transactional
    public PostsSaveResponseDto save(PostsSaveRequestDto dto) {

        Boards board = boardsService.findById(dto.getBoardId());
        Category category = categoryService.findById(dto.getCategoryId());
        Menu menu = menuService.findById(dto.getMenuId());

        Posts post = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .popularFlag(dto.getPopularFlag())
                .board(board)
                .category(category)
                .menu(menu)
                .build();

        return PostsSaveResponseDto.from(postsRepository.save(post));
    }

    @Transactional
    public PostsSaveResponseDto save(PostsSaveRequestDto dto, List<MultipartFile> images) {

        PostsSaveResponseDto responseDto = save(dto); // DB에 게시글 등록
        Posts post = findPostsById(responseDto.getPostId()); // 영속성 컨텍스트의 1차 캐시에서 DB접근 없이 방금 저장한 post를 조회할 수 있다.

        List<Long> uploadedImagesId =  imagesService.save(post, images); // 파일 저장 및 DB에 파일 정보 등록
        responseDto.setImageValues(uploadedImagesId);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> find(Pageable pageable) {
        Page<Posts> posts = postsRepository.findAll(pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryService.findById(categoryId);

        Page<Posts> posts = postsRepository.findByCategory(category, pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findByBoard(Long boardId, Pageable pageable) {
        Boards boards = boardsService.findById(boardId);

        Page<Posts> posts = postsRepository.findByBoard(boards, pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findByMenu(Long menuId, Pageable pageable) {
        Menu menu = menuService.findById(menuId);
        
        Page<Posts> posts = postsRepository.findByMenu(menu, pageable);

        return posts.map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public PostsFindByIdResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다. id=" + id));

        return PostsFindByIdResponseDto.from(post);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다. id=" + id));
        post.increaseViewCount();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다. id=" + id));

        Category category = categoryService.findById(dto.getCategoryId());

        posts.update(dto.getTitle(), dto.getContent(), category, dto.getPopularFlag());

        return posts.getId();
    }

    @Transactional
    public void delete(Long id) {
        postsRepository.deleteById(id);
    }


    /* 서비스 계층 내에서만 사용할 메소드들 */

    @Transactional(readOnly = true)
     Posts findPostsById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다. id=" + id));
    }
}
