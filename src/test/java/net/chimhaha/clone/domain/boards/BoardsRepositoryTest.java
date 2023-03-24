package net.chimhaha.clone.domain.boards;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BoardsRepositoryTest {

    @Autowired
    private BoardsRepository boardsRepository;

    @AfterEach
    public void cleanup() {
        boardsRepository.deleteAll();
    }

    static String name = "침착맨";
    static String description = "침착맨에 대해 이야기하는 게시판입니다";
    static Integer likeLimit = 10;

    @Test
    public void 게시판_이름으로_조회() {
        // given
        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .likeLimit(10)
                .build();

        boardsRepository.save(board);
        // when
        Boards categorychim =  boardsRepository.getReferenceByName(name).get();

        // then
        assertEquals(board.getDescription(), categorychim.getDescription());
    }

    @Test
    public void 게시판_등록() {
        // given
        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .likeLimit(10)
                .build();
        Long expectedId = 1L;
        // when
        Boards createdBoard = boardsRepository.save(board);

        // then
        assertAll(() -> assertEquals(expectedId, createdBoard.getId()),
                () -> assertEquals(board.getName(), createdBoard.getName()),
                () -> assertEquals(board.getDescription(), createdBoard.getDescription()),
                () -> assertEquals(board.getLikeLimit(), createdBoard.getLikeLimit())
        );
    }
}
