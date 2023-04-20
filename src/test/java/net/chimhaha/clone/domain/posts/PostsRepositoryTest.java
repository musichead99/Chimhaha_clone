package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PostsRepositoryTest {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    BoardsRepository boardsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostsRepository postsRepository;

    /* 단위 테스트 메소드 */
    @Test
    public void 게시글저장_불러오기() {
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

        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();
        Posts post = postsList.get(0);

        // then
        assertAll(() -> assertEquals("테스트 게시글", post.getTitle()),
                () -> assertEquals("테스트 본문", post.getContent()),
                () -> assertEquals("침착맨", post.getMenu().getName()),
                () -> assertEquals("침착맨", post.getBoard().getName()),
                () -> assertEquals("침착맨", post.getCategory().getName()),
                () -> assertEquals(0, post.getViews()),
                () -> assertEquals(true, post.getPopularFlag())
        );
    }

    @Test
    public void 페이징_게시글_전체_조회() {
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

        for(int i = 0; i < 5; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page,size);

        // when
        Page<Posts> posts = postsRepository.findAll(pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize())
        );
    }

    @Test
    public void 게시글_수정() {
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

        Category updatedCategory = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨 짤")
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

        // when
        Posts savedPost = postsRepository.save(post);
        savedPost.update("테스트 게시글 2", "테스트 본문 2", updatedCategory, false);
        Posts updatedPost = postsRepository.save(savedPost);

        // then
        assertAll(
                () -> assertEquals(savedPost.getId(), updatedPost.getId()),
                () -> assertEquals("테스트 게시글 2", updatedPost.getTitle()),
                () -> assertEquals("테스트 본문 2", updatedPost.getContent()),
                () -> assertEquals("침착맨 짤", updatedPost.getCategory().getName())
        );
    }

    @Test
    public void 게시글_삭제() {
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

        // when
        postsRepository.deleteById(post.getId());
        Optional<Posts> optionalPosts = postsRepository.findById(post.getId());

        // then
        assertFalse(optionalPosts.isPresent());
    }

    @Test
    public void 게시판별_게시글_조회() {
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

        int amount = 5;
        for(int i = 0; i < amount; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // when
        Page<Posts> posts = postsRepository.findByBoard(board, pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize()),
                () -> assertEquals(amount, posts.getNumberOfElements()),
                () -> assertEquals(board.getName(), posts.getContent().get(0).getBoard().getName())
        );
    }

    @Test
    public void 카테고리별_게시글_조회() {
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

        int amount = 5;
        for(int i = 0; i < amount; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // when
        Page<Posts> posts = postsRepository.findByCategory(category, pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize()),
                () -> assertEquals(5, posts.getNumberOfElements()),
                () -> assertEquals(board.getName(), posts.getContent().get(0).getBoard().getName())
        );
    }
}
