package net.chimhaha.clone.domain.comments;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class CommentsRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Test
    public void 댓글_등록하기() {
        // given
        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .build()
        );

        Posts post = postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build());

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build();

        // when
        Comments storedComment = commentsRepository.save(comment);

        // then
        assertAll(
                () -> assertNotNull(comment.getId()),
                () -> assertEquals("테스트 댓글", comment.getContent()),
                () -> assertEquals(post.getId(), comment.getPost().getId())
        );

    }

    @Test
    public void 대댓글_등록하기() {
        // given
        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .build()
        );

        Posts post = postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build());

        Comments parentComment = commentsRepository.save(Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build());

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 대댓글")
                .parent(parentComment)
                .build();

        // when
        Comments storedComment = commentsRepository.save(comment);

        // then
        assertAll(
                () -> assertNotNull(storedComment.getId()),
                () -> assertEquals("테스트 대댓글", storedComment.getContent()),
                () -> assertNotNull(storedComment.getParent())
        );

    }
}
