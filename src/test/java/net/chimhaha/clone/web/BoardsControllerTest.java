package net.chimhaha.clone.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.service.BoardsService;
import net.chimhaha.clone.web.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@WebMvcTest(controllers = BoardsController.class)
public class BoardsControllerTest {

    @Autowired
    private MockMvc mvc;

    /* @MockBean과 @Mock의 차이점?
    *  @Mock : springboot container에 적재되지 않는다. -> 직접 생성자를 통해 해당 객체를 주입하거나 @InjectMock어노테이션을 사용해 주입한다.
    *  @MockBean : Bean이 붙어 있다 -> springboot container에 bean으로써 적재된다. -> container가 자동으로 해당 빈을 사용하는 객체에게 주입
    *  쉽게 생각하면 WebMvcTest에서는 @MockBean, 다른 경우에는 @Mock를 사용한다고 한다. */
    @MockBean
    private BoardsService boardsService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    static String name = "침착맨";
    static String description = "침착맨에 대해 이야기하는 게시판입니다";
    static Integer likeLimit = 10;

    @Test
    public void 게시글_등록() throws Exception {
        // given
        BoardsSaveRequestDto dto = BoardsSaveRequestDto.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();
        Long boardId = 1L;

        given(boardsService.save(any(BoardsSaveRequestDto.class)))
                .willReturn(boardId);

        // when
        // then
        mvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(boardId.toString()));
    }

    @Test
    public void 게시판_전체_조회() throws Exception {
        // given
        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();
        Long boardId = 1L;
        ReflectionTestUtils.setField(board, "id", boardId);

        BoardsFindResponseDto dto = new BoardsFindResponseDto(board);
        List<BoardsFindResponseDto> dtoList = new ArrayList<>();
        dtoList.add(dto);

        given(boardsService.find())
                .willReturn(dtoList);

        // when
        // then
        mvc.perform(get("/boards"))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
                .andExpect(status().isOk());
    }

    @Test
    public void 게시판_수정() throws Exception {
        // given
        BoardsUpdateRequestDto dto = BoardsUpdateRequestDto.builder()
                .name(name)
                .description(description)
                .likeLimit(likeLimit)
                .build();

        Long boardId = 1L;

        given(boardsService.update(any(Long.class), any(BoardsUpdateRequestDto.class)))
                .willReturn(1L);
        // when
        // then
        mvc.perform(put("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(content().string(boardId.toString()))
                .andExpect(status().isOk());
    }
}
