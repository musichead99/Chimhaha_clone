package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    BoardsRepository boardsRepository;

    String title = "테스트 게시글";
    String content = "테스트 본문";
    String subject = "침착맨";
    Boolean flag = true;

    Boards board = Boards.builder()
            .name("침착맨")
            .description("침착맨에 대해 이야기하는 게시판입니다")
            .likeLimit(10)
            .build();

    /* @AfterEach를 단 메소드는 매 단위 테스트가 끝날 때마다 호출 */
    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
        boardsRepository.deleteAll();
    }

    @BeforeEach
    public void settup() {
        boardsRepository.save(board);
    }

    /* 단위 테스트 메소드 */
    @Test
    public void 게시글저장_불러오기() {
        // given
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();
        Posts post = postsList.get(0);

        // then
        assertAll(() -> assertEquals(title, post.getTitle()),
                () -> assertEquals(content, post.getContent()),
                () -> assertEquals(subject, post.getSubject()),
                () -> assertEquals(0,post.getViews()),
                () -> assertEquals(flag, post.getPopularFlag())
        );
    }

    @Test
    public void 게시글_수정하기() {
        // given
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build());

        // when
        Posts savedPosts = postsRepository.findAll().get(0);
        savedPosts.update("테스트 게시글 2", "테스트 본문 2", "쇼츠 요청", flag);
        Posts updatedPosts = postsRepository.save(savedPosts);

        assertEquals("테스트 게시글 2", updatedPosts.getTitle());
        assertEquals("테스트 본문 2", updatedPosts.getContent());
        assertEquals("쇼츠 요청", updatedPosts.getSubject());
    }

    @Test
    public void 게시글_삭제하기() {
        // given
        Posts posts = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build());

        // when
        postsRepository.deleteById(posts.getId());
        Optional<Posts> optionalPosts = postsRepository.findById(posts.getId());

        // given
        assertFalse(optionalPosts.isPresent());
    }

    @Test
    public void 게시판명으로_게시글_조회하기() {
        // given
        Posts post = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build());

        // when
        List<Posts> categorizedPosts = postsRepository.findByBoard(board);
        Posts categorizedPost = categorizedPosts.get(0);

        // then
        assertEquals(post.getBoard(), categorizedPost.getBoard());
    }

    @Test
    public void 카테고리로_게시글_불러오기() {
        // given
        Posts post = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build());

        // when
        Posts postBySubject = postsRepository.findBySubject(subject).get(0);

        // then
        assertEquals(postBySubject.getSubject(), subject);
    }
}
