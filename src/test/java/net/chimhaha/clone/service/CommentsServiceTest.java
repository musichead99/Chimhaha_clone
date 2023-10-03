package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.comments.CommentsRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.dto.comments.CommentsUpdateRequestDto;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class CommentsServiceTest {

    @Mock
    private PostsService postsService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private CommentsService commentsService;

    @Test
    public void 댓글_등록하기() {
        // given
        Posts post = mock(Posts.class);
        Member member = mock(Member.class);

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

        given(commentsRepository.save(any(Comments.class)))
                .willReturn(comment);

        // when
        Long createdCommentId = commentsService.save(dto, member, post);

        // then
        assertAll(
                () -> assertEquals(1L, createdCommentId),
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

        given(commentsRepository.findAllByPost(any(Posts.class), any(Pageable.class)))
                .willReturn(pagedComments);

        // when
        Page<Comments> resultCommentsList = commentsService.findByPost(pageable, post);

        // then
        assertAll(
                () -> assertEquals(0, resultCommentsList.getNumber()),
                () -> assertEquals(20, resultCommentsList.getSize()),
                () -> assertEquals(1, resultCommentsList.getNumberOfElements()),
                () -> assertEquals(1, resultCommentsList.getContent().get(0).getId())
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

        given(commentsRepository.findAllByPost(any(Posts.class), any(Pageable.class)))
                .willReturn(pagedComments);

        // when
        Page<Comments> resultCommentsList = commentsService.findByPost(pageable, post);

        // then
        assertAll(
                () -> assertEquals(0, resultCommentsList.getNumber()),
                () -> assertEquals(20, resultCommentsList.getSize()),
                () -> assertEquals(3, resultCommentsList.getNumberOfElements()), // 계층화 작업이 이루어지지 않음
                () -> assertNotNull(resultCommentsList.getContent().get(0).getChildren()),
                () -> assertEquals(0, resultCommentsList.getContent().get(0).getChildren().size()) // 계층화 작업이 이루어지지 않음
        );
    }

    @Test
    public void 댓글_수정하기() {
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

        CommentsUpdateRequestDto dto = CommentsUpdateRequestDto.builder()
                .content("테스트 댓글 수정")
                .build();

        // when
        Long updatedCommentId = commentsService.update(dto, comment);

        // then
        assertAll(
                () -> assertEquals(1, updatedCommentId)
        );
    }

    @Test
    public void 대댓글이_달리지_않은_댓글_삭제하기() {
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

        willDoNothing().given(commentsRepository).delete(any(Comments.class));

        // when
        commentsService.delete(comment);

        // then
        assertAll(
                () -> verify(commentsRepository, times(1)).delete(any(Comments.class))
        );

    }

    /*
     *  테스트 댓글
     *   ㄴ 테스트 대댓글 1
     * */
    @Test
    public void 대댓글이_달린_부모댓글_삭제하기() {
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
        ReflectionTestUtils.setField(parentComment,"isDeleted", false);

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 대댓글 1")
                .parent(parentComment)
                .build();
        ReflectionTestUtils.setField(comment,"id", 2L);
        ReflectionTestUtils.setField(comment,"isDeleted", false);

        parentComment.getChildren().add(comment);


        // when
        commentsService.delete(parentComment);

        // then
        assertAll(
                () -> verify(commentsRepository, never()).delete(any(Comments.class))
        );

    }

    /*
     *  테스트 댓글
     *   ㄴ 테스트 대댓글 1
     * */
    @Test
    public void 부모댓글이_삭제되지_않은_대댓글_삭제하기() {
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
        ReflectionTestUtils.setField(parentComment,"isDeleted", false);

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(parentComment)
                .build();
        ReflectionTestUtils.setField(comment,"id", 2L);
        ReflectionTestUtils.setField(comment,"isDeleted", false);

        parentComment.getChildren().add(comment);

        willDoNothing().given(commentsRepository).delete(any(Comments.class));

        // when
        commentsService.delete(comment);

        // then
        assertAll(
                () -> verify(commentsRepository, times(1)).delete(any(Comments.class))
        );

    }

    /*
     *  테스트 댓글
     *   ㄴ 테스트 대댓글 1
     * */
    @Test
    public void 부모댓글이_삭제된_대댓글_삭제하기() {
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
        ReflectionTestUtils.setField(parentComment,"isDeleted", true);

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 대댓글 1")
                .parent(parentComment)
                .build();
        ReflectionTestUtils.setField(comment,"id", 2L);
        ReflectionTestUtils.setField(comment,"isDeleted", false);

        parentComment.getChildren().add(comment);

        willDoNothing().given(commentsRepository).delete(any(Comments.class));

        // when
        commentsService.delete(comment);

        // then
        assertAll(
                () -> verify(commentsRepository, times(1)).delete(any(Comments.class))
        );

    }
}
