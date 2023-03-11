package net.chimhaha.clone.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String tag = "주펄";
        Short flag = 1;

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .tag(tag)
                .popularFlag(flag)
                .build());

        List<Posts> postsList = postsRepository.findAll();

        Posts post = postsList.get(0);

        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
        assertEquals(tag, post.getTag());
        assertEquals(0,post.getViews());
        assertEquals(flag, post.getPopularFlag());
    }
}
