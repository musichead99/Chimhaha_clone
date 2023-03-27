package net.chimhaha.clone.domain.boards;

import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

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
                .likeLimit(likeLimit)
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
                .likeLimit(likeLimit)
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

    @Test
    public void 게시판_수정() {
        // given
        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .likeLimit(10)
                .build();

        BoardsUpdateRequestDto dto = BoardsUpdateRequestDto.builder()
                .name("대인국(주펄)")
                .description("주호민 자치령")
                .likeLimit(20)
                .build();

        // when
        Boards savedBoard = boardsRepository.save(board);
        savedBoard.update(dto);
        Boards updatedBoard = boardsRepository.save(savedBoard);

        // then
        assertAll(() -> assertEquals(savedBoard.getId(), updatedBoard.getId()),
                () -> assertEquals("대인국(주펄)", updatedBoard.getName()),
                () -> assertEquals("주호민 자치령", updatedBoard.getDescription()),
                () -> assertEquals(20, updatedBoard.getLikeLimit()));
    }

    @Test
    public void 게시판_삭제() {

    }
}
