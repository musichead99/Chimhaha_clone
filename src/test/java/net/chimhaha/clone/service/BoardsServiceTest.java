package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.dto.boards.BoardsUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@ExtendWith(MockitoExtension.class)
public class BoardsServiceTest {

    @Mock
    private BoardsRepository boardsRepository;

    @InjectMocks // 위의 mock객체들을 주입
    private BoardsService boardsService;

    static String name = "침착맨";
    static String description = "침착맨에 대해 이야기하는 게시판입니다";
    static Integer likeLimit = 10;

    @Test
    public void 게시판_등록() {
        // given
        BoardsSaveRequestDto dto = BoardsSaveRequestDto.builder()
                .menuId(1L)
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();

        Menu menu = mock(Menu.class);
        Boards board = mock(Boards.class);
        Member member = mock(Member.class);

        given(boardsRepository.save(any(Boards.class)))
                .willReturn(board);
        given(board.getId())
                .willReturn(1L);

        // when
        Long createdBoardId = boardsService.save(dto, menu, member);

        // then
        assertEquals(1L, createdBoardId);
    }

    @Test
    public void 게시판_전체_조회() {
        // given
        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();

        List<Boards> boards = new ArrayList<>();
        boards.add(board);

        given(boardsRepository.findAll())
                .willReturn(boards);
        // when
        List<Boards> dtoList =  boardsService.find();

        // then
        assertAll(
                () -> assertEquals(1, dtoList.size()),
                () -> assertEquals(board.getName(), dtoList.get(0).getName()),
                () -> assertEquals(board.getDescription(), dtoList.get(0).getDescription()),
                () -> assertEquals(board.getLikeLimit(), dtoList.get(0).getLikeLimit())
        );

    }

    @Test
    public void 게시판_수정() {
        // given
        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();

        BoardsUpdateRequestDto dto = BoardsUpdateRequestDto.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();

        Long boardId = 1L;
        ReflectionTestUtils.setField(board, "id", boardId);

        // when
        Long updatedBoardId = boardsService.update(board, dto);

        // then
        assertAll(
                () -> assertEquals(boardId,updatedBoardId)
        );
    }
}
