package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    public void 페이징_게시글_전체_조회() {
        // given
        Posts post = Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build();

        for(int i = 0; i < 5; i++) {
            postsRepository.save(post);
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
        Posts post = Posts.builder()
                .title(title)
                .content(content)
                .board(board)
                .subject(subject)
                .popularFlag(flag)
                .build();

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title("테스트 게시글 2")
                .content("테스트 본문 2")
                .subject("쇼츠 요청")
                .popularFlag(flag)
                .build();

        // when
        Posts savedPost = postsRepository.save(post);
        savedPost.update(dto);
        Posts updatedPost = postsRepository.save(savedPost);

        // then
        assertAll(
                () -> assertEquals(savedPost.getId(), updatedPost.getId()),
                () -> assertEquals(dto.getTitle(), updatedPost.getTitle()),
                () -> assertEquals(dto.getContent(), updatedPost.getContent()),
                () -> assertEquals(dto.getSubject(), updatedPost.getSubject())
        );
    }

    @Test
    public void 게시글_삭제() {
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

        // then
        assertFalse(optionalPosts.isPresent());
    }

    @Test
    public void 게시판별_게시글_조회() {
        // given
        int amount = 5;
        for(int i = 0; i < amount; i++) {
            postsRepository.save(Posts.builder()
                    .title(title)
                    .content(content)
                    .board(board)
                    .subject(subject)
                    .popularFlag(flag)
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
