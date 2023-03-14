package net.chimhaha.clone.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    /* @AfterEach를 단 메소드는 매 단위 테스트가 끝날 때마다 호출 */
    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }

    /* 단위 테스트 메소드 */
    @Test
    public void 게시글저장_불러오기() {
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String category = "주펄";
        Short flag = 1;

        // given
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();
        Posts post = postsList.get(0);

        // then
        assertAll(() -> assertEquals(title, post.getTitle()),
                () -> assertEquals(content, post.getContent()),
                () -> assertEquals(category, post.getCategory()),
                () -> assertEquals(0,post.getViews()),
                () -> assertEquals(flag, post.getPopularFlag())
        );
    }

    @Test
    public void 게시글_수정하기() {
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String category = "주펄";
        Short flag = 1;

        // given
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build());

        // when
        Posts savedPosts = postsRepository.findAll().get(0);
        savedPosts.update("테스트 게시글 2", "테스트 본문 2", "쇼츠 요청");
        Posts updatedPosts = postsRepository.save(savedPosts);

        assertEquals("테스트 게시글 2", updatedPosts.getTitle());
        assertEquals("테스트 본문 2", updatedPosts.getContent());
        assertEquals("쇼츠 요청", updatedPosts.getCategory());
    }

    @Test
    public void 게시글_삭제하기() {
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String category = "주펄";
        Short flag = 1;

        // given
        Posts posts = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build());

        // when
        postsRepository.delete(posts);
        Optional<Posts> optionalPosts = postsRepository.findById(posts.getId());

        // given
        assertFalse(optionalPosts.isPresent());
    }
}
