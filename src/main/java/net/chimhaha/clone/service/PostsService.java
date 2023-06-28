package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.dto.posts.*;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.utils.FileUploadUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final BoardsService boardsService;
    private final CategoryService categoryService;
    private final MenuService menuService;
    private final ImagesService imagesService;
    private final FileUploadUtils fileUploadUtils;

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER, MemberRole.ROLES.USER})
    @Transactional
    public PostsSaveResponseDto save(PostsSaveRequestDto dto) {
        Boards board = boardsService.findById(dto.getBoardId());
        Category category = categoryService.findById(dto.getCategoryId());
        Menu menu = menuService.findById(dto.getMenuId());
        List<Images> images = imagesService.findByIdIn(dto.getImageIdList());

        Posts post = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .popularFlag(dto.getPopularFlag())
                .board(board)
                .category(category)
                .menu(menu)
                .build();

        post.addAttachedImages(images);
        images.forEach(image -> image.attachedToPost(post));

        PostsSaveResponseDto responseDto = PostsSaveResponseDto.from(postsRepository.save(post));
        responseDto.setImageValues(images.stream()
                .map(Images::getId)
                .collect(Collectors.toList()));

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
        Posts post = this.findPostsById(id);
        post.increaseViewCount();

        return PostsFindByIdResponseDto.from(post);
    }

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER, MemberRole.ROLES.USER})
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto) {
        Posts post = this.findPostsById(id);
        List<Images> images = imagesService.findByIdIn(dto.getImageIdList(), post);
        Category category = categoryService.findById(dto.getCategoryId());

        post.update(
                dto.getTitle(),
                dto.getContent(),
                category,
                images,
                dto.getPopularFlag()
        );

        images.stream()
                .filter(image -> image.getPost() == null)
                .forEach(image -> image.attachedToPost(post));

        return post.getId();
    }

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER, MemberRole.ROLES.USER})
    @Transactional
    public void delete(Long id) {
        Posts post = this.findPostsById(id);
        List<Images> images = imagesService.findByPost(post);

        postsRepository.delete(post);

        images.stream()
                .map(Images::getStoredFilePath)
                .map(File::new)
                .forEach(fileUploadUtils::delete);
    }


    /* 서비스 계층 내에서만 사용할 메소드들 */

    @Transactional(readOnly = true)
    public Posts findPostsById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    }
}
