package net.chimhaha.clone.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.service.CategoryService;
import net.chimhaha.clone.web.dto.category.CategorySaveRequestDto;
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
@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    String name = "침착맨";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 카테고리_등록() throws Exception {
        // given
        Long boardId = 1L;
        Long categoryId = 1L;

        CategorySaveRequestDto dto = CategorySaveRequestDto.builder()
                .name(name)
                .boardId(boardId)
                .build();

        given(categoryService.save(any(CategorySaveRequestDto.class)))
                .willReturn(categoryId);
        // when
        // then
        mvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(content().string(categoryId.toString()))
                .andExpect(status().isCreated());
    }
}
