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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class CommentsRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

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

    @Test
    public void 댓글_조회하기() {
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

        Comments comment = commentsRepository.save(Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build());

        Pageable pageable = PageRequest.of(0, 20);

        // when
        Page<Comments> pagedComments = commentsRepository.findAllByPost(post, pageable);
        List<Comments> comments = pagedComments.getContent();

        // then
        assertAll(
                () -> assertEquals(1, pagedComments.getNumberOfElements()),
                () -> assertEquals(0, pagedComments.getNumber()),
                () -> assertEquals("테스트 댓글", comments.get(0).getContent())
        );
    }

    @Test
    public void 부모댓글_댓글_자식댓글_모두_조회하기() {
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

        Comments parent = commentsRepository.save(Comments.builder()
                .post(post)
                .content("테스트 부모댓글")
                .parent(null)
                .build());

        Comments comment = commentsRepository.save(Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(parent)
                .build());

        Comments child = commentsRepository.save(Comments.builder()
                .post(post)
                .content("테스트 자식댓글")
                .parent(comment)
                .build());

        testEntityManager.clear(); // 영속성 컨텍스트에 캐시된 엔티티들을 제거한다.

        // when
        Comments readComment = commentsRepository.findByIdWithParents(comment.getId()).get();

        // then
        assertAll(
                () -> assertNotNull(readComment.getParent()),
                () -> assertEquals(1, readComment.getChildren().size()),
                () -> assertEquals("테스트 댓글", readComment.getContent()),
                () -> assertEquals("테스트 부모댓글", readComment.getParent().getContent()),
                () -> assertEquals("테스트 자식댓글", readComment.getChildren().get(0).getContent())
        );

    }

    @Test
    public void 대댓글_조회하기() {
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

        Posts post = postsRepository.save(
                Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .build());

        Comments parentComment = commentsRepository.save(
                Comments.builder()
                    .post(post)
                    .content("테스트 댓글")
                    .parent(null)
                    .build());

        Comments comment = commentsRepository.save(
                Comments.builder()
                        .post(post)
                        .content("테스트 대댓글")
                        .parent(parentComment)
                        .build());

        Pageable pageable = PageRequest.of(0, 20);

        // when
        Page<Comments> pagedComments = commentsRepository.findAllByPost(post, pageable);
        List<Comments> comments = pagedComments.getContent();

        // then
        assertAll(
                () -> assertEquals(2, pagedComments.getNumberOfElements()),
                () -> assertEquals(0, pagedComments.getNumber()),
                () -> assertNull(comments.get(0).getParent()),
                () -> assertNotNull(comments.get(1).getParent()),
                () -> assertEquals(comments.get(1).getParent().getId(), comments.get(0).getId()),
                () -> assertEquals("테스트 대댓글", comments.get(1).getContent())
        );
    }

    @Test
    public void 댓글_수정하기() {
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

        Posts post = postsRepository.save(
                Posts.builder()
                        .title("테스트 게시글")
                        .content("테스트 본문")
                        .menu(menu)
                        .board(board)
                        .category(category)
                        .popularFlag(true)
                        .build());

        Comments comment = commentsRepository.save(
                Comments.builder()
                        .post(post)
                        .content("테스트 대댓글")
                        .parent(null)
                        .build());

        // when
        comment.update("테스트 댓글 수정");
        Comments updatedComment = commentsRepository.save(comment);

        // then
        assertAll(
                () -> assertEquals("테스트 댓글 수정", updatedComment.getContent())
        );
    }

    @Test
    public void 댓글_삭제하기() {
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

        Comments comment = commentsRepository.save(Comments.builder()
                .post(post)
                .content("테스트 부모댓글")
                .parent(null)
                .build());
        Long commentId = comment.getId();

        // when
        commentsRepository.delete(comment);
        Optional<Comments> optionalComment = commentsRepository.findById(commentId);

        // then
        assertFalse(optionalComment.isPresent());
    }
}
