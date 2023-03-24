package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardsServiceTest {

    @Mock
    private BoardsRepository boardsRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks // 위의 mock객체들을 주입
    private BoardsService boardsService;

    static String name = "침착맨";
    static String description = "침착맨에 대해 이야기하는 게시판입니다";
    static Integer likeLimit = 10;

    @Test
    public void 게시판_등록() {
        // given
        BoardsSaveRequestDto dto = BoardsSaveRequestDto.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();

        Boards board = Boards.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .likeLimit(dto.getLikeLimit())
                .build();
        Long boardId = 1L;
        ReflectionTestUtils.setField(board, "id", boardId);

        given(boardsRepository.save(any(Boards.class)))
                .willReturn(board);

        // when
        Long createdBoardId = boardsService.save(dto);

        // then
        assertEquals(boardId, createdBoardId);
    }
}
