package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.comments.CommentsRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.web.dto.comments.CommentsSaveRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class CommentsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private CommentsRepository commentsRepository;

    @InjectMocks
    private CommentsService commentsService;

    @Test
    public void 댓글_등록하기() {
        // given
        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        Boards board = Boards.builder()
                .menu(menu)
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();
        ReflectionTestUtils.setField(board, "id", 1L);

        Category category = Category.builder()
                .board(board)
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(category, "id", 1L);

        Posts post = Posts.builder()
                .title("테스트 글")
                .content("테스트 내용")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build();
        ReflectionTestUtils.setField(post,"id", 1L);

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build();
        ReflectionTestUtils.setField(comment,"id", 1L);

        CommentsSaveRequestDto dto = CommentsSaveRequestDto.builder()
                .postId(1L)
                .content("테스트 댓글")
                .parentId(null)
                .build();

        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(post));
        given(commentsRepository.save(any(Comments.class)))
                .willReturn(comment);

        // when
        Long createdCommentId = commentsService.save(dto);

        // then
        assertAll(
                () -> assertEquals(1L, createdCommentId),
                () -> verify(postsRepository, times(1)).findById(any(Long.class)),
                () -> verify(commentsRepository, times(1)).save(any(Comments.class))
        );
    }

    @Test
    public void 댓글_조회하기() {
        // given
        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        Boards board = Boards.builder()
                .menu(menu)
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();
        ReflectionTestUtils.setField(board, "id", 1L);

        Category category = Category.builder()
                .board(board)
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(category, "id", 1L);

        Posts post = Posts.builder()
                .title("테스트 글")
                .content("테스트 내용")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build();
        ReflectionTestUtils.setField(post,"id", 1L);

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build();
        ReflectionTestUtils.setField(comment,"id", 1L);

        List<Comments> comments = new ArrayList<>();
        comments.add(comment);

        Pageable pageable = PageRequest.of(0, 20);
        Page<Comments> pagedComments = new PageImpl<>(comments, pageable, comments.size());

        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.of(post));
        given(commentsRepository.findAllByPost(any(Posts.class), any(Pageable.class)))
                .willReturn(pagedComments);

        // when
        Page<CommentsFindByPostResponseDto> dtoList = commentsService.findByPost(1L, pageable);

        // then
        assertAll(
                () -> assertEquals(0, dtoList.getNumber()),
                () -> assertEquals(20, dtoList.getSize()),
                () -> assertEquals(1, dtoList.getNumberOfElements()),
                () -> assertEquals(1, dtoList.getContent().get(0).getId())
        );
    }

    /*
    *  테스트 댓글
    *   ㄴ 테스트 대댓글 1
    *   ㄴ 테스트 대댓글 2
    * */
    @Test
    public void 대댓글_조회하기() {
        // given
        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        Boards board = Boards.builder()
                .menu(menu)
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();
        ReflectionTestUtils.setField(board, "id", 1L);

        Category category = Category.builder()
                .board(board)
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(category, "id", 1L);

        Posts post = Posts.builder()
                .title("테스트 글")
                .content("테스트 내용")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build();
        ReflectionTestUtils.setField(post,"id", 1L);

        Comments parentComment = Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build();
        ReflectionTestUtils.setField(parentComment,"id", 1L);

        Comments comment_1 = Comments.builder()
                .post(post)
                .content("테스트 대댓글 1")
                .parent(parentComment)
                .build();
        ReflectionTestUtils.setField(parentComment,"id", 2L);

        Comments comment_2 = Comments.builder()
                .post(post)
                .content("테스트 대댓글 2")
                .parent(parentComment)
                .build();
        ReflectionTestUtils.setField(parentComment,"id", 3L);

        List<Comments> comments = new ArrayList<>();
        comments.add(parentComment);
        comments.add(comment_1);
        comments.add(comment_2);

        Pageable pageable = PageRequest.of(0, 20);
        Page<Comments> pagedComments = new PageImpl<>(comments, pageable, comments.size());

        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.of(post));
        given(commentsRepository.findAllByPost(any(Posts.class), any(Pageable.class)))
                .willReturn(pagedComments);

        // when
        Page<CommentsFindByPostResponseDto> dtoList = commentsService.findByPost(1L, pageable);

        // then
        assertAll(
                () -> assertEquals(0, dtoList.getNumber()),
                () -> assertEquals(20, dtoList.getSize()),
                () -> assertEquals(1, dtoList.getNumberOfElements()),
                () -> assertNotNull(dtoList.getContent().get(0).getChildren()),
                () -> assertEquals(2, dtoList.getContent().get(0).getChildren().size())
        );
    }
}
