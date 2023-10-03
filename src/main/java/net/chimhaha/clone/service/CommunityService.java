package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.dto.boards.BoardsUpdateRequestDto;
import net.chimhaha.clone.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.dto.category.CategoryUpdateRequestDto;
import net.chimhaha.clone.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.dto.comments.CommentsUpdateRequestDto;
import net.chimhaha.clone.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.dto.menu.MenuUpdateRequestDto;
import net.chimhaha.clone.dto.posts.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전체적으로 서비스들을 총괄하는 파사드 패턴을 적용한 객체
 * CRUD를 통해 가져온 도메인들을 DTO로 변환하거나 다른 서비스에서 필요한 객체를 조회하는 등
 * 실제 서비스 객체들이 하지 않아도 될 일을 분리하기 위해서 작성하기로 함
 * 이로서 각 서비스 객체들은 다른 서비스에 대한 의존성을 가지지 않아도 됨
 */

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final MenuService menuService;
    private final BoardsService boardsService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final PostsService postsService;
    private final CommentsService commentsService;
    private final ImagesService imagesService;

    /* Menu 관련 */
    
    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long saveMenu(MenuSaveRequestDto dto, Long memberId) {
        Member member = memberService.findById(memberId);
        return menuService.save(dto, member);
    }

    @Transactional(readOnly = true)
    public List<MenuFindResponseDto> findMenu() {
        List<Menu> menuList = menuService.find();

        return menuList.stream()
                .map(MenuFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long updateMenu(Long id, MenuUpdateRequestDto dto) {
        return menuService.update(id, dto);
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public void deleteMenu(Long id) {
        menuService.delete(id);
    }
    
    /* Board 관련 */

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long saveBoards(BoardsSaveRequestDto dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Menu menu = menuService.findById(dto.getMenuId());

        return boardsService.save(dto, menu, member);
    }

    @Transactional(readOnly = true)
    public List<BoardsFindResponseDto> findBoards() {
        List<Boards> boardsList = boardsService.find();

        return boardsList.stream()
                .map(BoardsFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public Long updateBoards(Long id, BoardsUpdateRequestDto dto) {
        Boards board = boardsService.findById(id);
        return boardsService.update(board, dto);
    }

    @Secured({MemberRole.ROLES.ADMIN})
    @Transactional
    public void deleteBoards(Long id) {
        boardsService.delete(id);
    }

    /* Category 관련 */
    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER})
    @Transactional
    public Long saveCategory(CategorySaveRequestDto dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Boards board = boardsService.findById(dto.getBoardId());

        return categoryService.save(dto, member, board);
    }

    @Transactional(readOnly = true)
    public List<CategoryFindResponseDto> findCategory() {
        List<Category> categoryList = categoryService.find();

        return categoryList.stream()
                .map(CategoryFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER})
    @Transactional
    public Long updateCategory(Long id, CategoryUpdateRequestDto dto) {
        Boards board = boardsService.findById(dto.getBoardId());
        Category category = categoryService.findById(id);

        return categoryService.update(board, category, dto);
    }

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER})
    @Transactional
    public void deleteCategory(Long id) {
        categoryService.delete(id);
    }

    /* Posts 관련 */
    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.MANAGER, MemberRole.ROLES.ADMIN})
    @Transactional
    public PostsSaveResponseDto savePost(PostsSaveRequestDto dto, Long userId) {
        Member member = memberService.findById(userId);
        Boards board = boardsService.findById(dto.getBoardId());
        Category category = categoryService.findById(dto.getCategoryId());
        Menu menu = menuService.findById(dto.getMenuId());
        List<Images> images = imagesService.findByIdIn(dto.getImageIdList());

        Posts savedPost = postsService.save(member, board, category, menu, images, dto);

        PostsSaveResponseDto responseDto = PostsSaveResponseDto.from(savedPost);
        responseDto.setImageValues(images.stream()
                .map(Images::getId)
                .collect(Collectors.toList()));

        return responseDto;
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findPosts(Pageable pageable) {

        return postsService.find(pageable)
                .map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryService.findById(categoryId);

        return postsService.findByCategory(category, pageable)
                .map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findPostsByBoard(Long boardId, Pageable pageable) {
        Boards board = boardsService.findById(boardId);

        return postsService.findByBoard(board, pageable)
                .map(PostsFindResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostsFindResponseDto> findPostsByMenu(Long menuId, Pageable pageable) {
        Menu menu = menuService.findById(menuId);

        return postsService.findByMenu(menu, pageable)
                .map(PostsFindResponseDto::from);
    }

    @Transactional
    public PostsFindByIdResponseDto findPostById(Long id) {
        return PostsFindByIdResponseDto.from(postsService.findPostsById(id));
    }

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER, MemberRole.ROLES.USER})
    @Transactional
    public Long updatePost(Long id, PostsUpdateRequestDto dto) {
        Posts post = postsService.findById(id);
        List<Images> images = imagesService.findByIdIn(dto.getImageIdList());
        Category category = categoryService.findById(dto.getCategoryId());

        Posts updatedPost = postsService.update(post, images, category, dto);

        return updatedPost.getId();
    }

    @Secured({MemberRole.ROLES.ADMIN, MemberRole.ROLES.MANAGER, MemberRole.ROLES.USER})
    @Transactional
    public void deletePost(Long id) {
        Posts post = postsService.findById(id);
        List<Images> images = imagesService.findByPost(post);

        postsService.delete(post, images);
    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.MANAGER, MemberRole.ROLES.ADMIN})
    @Transactional
    public Long saveComment(CommentsSaveRequestDto dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Posts post = postsService.findPostsById(dto.getPostId());

        return commentsService.save(dto, member, post);
    }

    @Transactional(readOnly = true)
    public Page<CommentsFindByPostResponseDto> findCommentsByPost(Long postId, Pageable pageable) {
        /* 해당 게시글에 달린 댓글을 조회하기 위해 게시글 조회 */
        Posts post = postsService.findPostsById(postId);

        Page<Comments> comments = commentsService.findByPost(pageable, post);
        List<CommentsFindByPostResponseDto> dtoList = commentsService.toHierarchy(comments);

        /* 계층 구조로 변환이 완료된 댓글 리스트 PageImpl객체로 변환 */
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.MANAGER, MemberRole.ROLES.ADMIN})
    @Transactional
    public Long updateComment(Long id, CommentsUpdateRequestDto dto) {
        Comments comment = commentsService.findById(id);

        return commentsService.update(dto, comment);
    }

    @Secured({MemberRole.ROLES.USER, MemberRole.ROLES.MANAGER, MemberRole.ROLES.ADMIN})
    @Transactional
    public void deleteComment(Long id) {
        Comments comment = commentsService.findById(id);

        commentsService.delete(comment);
    }
}
