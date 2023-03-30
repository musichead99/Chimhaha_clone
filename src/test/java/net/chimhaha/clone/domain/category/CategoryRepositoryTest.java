package net.chimhaha.clone.domain.category;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BoardsRepository boardsRepository;

    Boards board = Boards.builder()
            .name("침착맨")
            .description("침착맨에 대해 이야기하는 게시판입니다")
            .likeLimit(10)
            .build();

    String name = "침착맨";

    @BeforeEach
    public void setup() {
        boardsRepository.save(board);
    }

    @AfterEach
    public void cleanup() {
        categoryRepository.deleteAll();
        boardsRepository.deleteAll();
    }

    @Test
    public void 카테고리_등록() {
        // given
        Category category = Category.builder()
                .name(name)
                .board(board)
                .build();

        // when
        Category storedCategory = categoryRepository.save(category);

        // then
        assertAll(
                () -> assertEquals(1, storedCategory.getId()),
                () -> assertEquals(category.getName(), storedCategory.getName()),
                () -> assertEquals(category.getBoard().getName(), storedCategory.getBoard().getName())
        );
    }
}