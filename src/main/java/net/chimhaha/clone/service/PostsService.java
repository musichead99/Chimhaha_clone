package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.member.Member;
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
    private final FileUploadUtils fileUploadUtils;

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER, MemberRole.ROLES.USER})
    @Transactional
    public Posts save(
            Member member, Boards board, Category category, Menu menu, List<Images> images, PostsSaveRequestDto dto) {

        Posts post = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .popularFlag(dto.getPopularFlag())
                .board(board)
                .category(category)
                .menu(menu)
                .member(member)
                .build();

        post.addAttachedImages(images);
        images.forEach(image -> image.attachedToPost(post));

        return postsRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<Posts> find(Pageable pageable) {
        return postsRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Posts> findByCategory(Category category, Pageable pageable) {
        return postsRepository.findByCategory(category, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Posts> findByBoard(Boards board, Pageable pageable) {
        return postsRepository.findByBoard(board, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Posts> findByMenu(Menu menu, Pageable pageable) {
        return postsRepository.findByMenu(menu, pageable);
    }

    @Transactional
    public Posts findById(Long id) {
        Posts post = this.findPostsById(id);
        post.increaseViewCount(); // 더티 체킹을 사용한 조회수 증가 기능 때문에 readOnly를 사용하지 않음

        return post;
    }

    @Transactional
    public Posts update(Posts post, List<Images> images, Category category, PostsUpdateRequestDto dto) {
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

        return post;
    }

    @Transactional
    public void delete(Posts post, List<Images> images) {

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
