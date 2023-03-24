package net.chimhaha.clone.domain.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void cleanup() {
        categoryRepository.deleteAll();
    }

    @Test
    public void 카테고리_이름으로_카테고리_조회() {
        // given
        Category category = Category.builder()
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();

        categoryRepository.save(category);
        // when
        Category categorychim =  categoryRepository.getReferenceByName("침착맨").get();

        // then
        assertEquals(category.getDescription(), categorychim.getDescription());
    }
}
