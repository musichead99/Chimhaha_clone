package net.chimhaha.clone.domain.boards;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BoardsRepositoryTest {

    @Autowired
    private BoardsRepository boardsRepository;

    @AfterEach
    public void cleanup() {
        boardsRepository.deleteAll();
    }

    @Test
    public void 카테고리_이름으로_카테고리_조회() {
        // given
        Boards board = Boards.builder()
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();

        boardsRepository.save(board);
        // when
        Boards categorychim =  boardsRepository.getReferenceByName("침착맨").get();

        // then
        assertEquals(board.getDescription(), categorychim.getDescription());
    }
}
