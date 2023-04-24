package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.comments.CommentsRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.comments.CommentsSaveRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
