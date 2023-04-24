package net.chimhaha.clone.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.service.CommentsService;
import net.chimhaha.clone.web.dto.comments.CommentsSaveRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = CommentsController.class)
public class CommentsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentsService commentsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 댓글_등록하기() throws Exception {
        // given
        CommentsSaveRequestDto dto = CommentsSaveRequestDto.builder()
                .postId(1L)
                .content("테스트 댓글")
                .parentId(null)
                .build();

        given(commentsService.save(any(CommentsSaveRequestDto.class)))
                .willReturn(1L);

        // when
        // then
        mvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

}
