package net.chimhaha.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.service.CategoryService;
import net.chimhaha.clone.controller.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.controller.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.controller.dto.category.CategoryUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    String name = "침착맨";
    String boardName = "침착맨";

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().string(categoryId.toString()))
                .andExpect(status().isCreated());
    }

    @Test
    public void 카테고리_전체_조회() throws Exception {
        // given
        List<CategoryFindResponseDto> dtoList = new LinkedList<>();
        int amount = 5;

        Long categoryId = 1L;
        Long boardId = 1L;

        for(int i = 0; i < amount; i++) {
            dtoList.add(new CategoryFindResponseDto(
                    categoryId,
                    name,
                    boardId,
                    boardName));
        }

        given(categoryService.find())
                .willReturn(dtoList);

        // when
        // then
        mvc.perform(get("/category")
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
                .andExpect(status().isOk());

    }

    @Test
    public void 카테고리_수정() throws Exception {
        // given
        Long boardId = 1L;
        Long categoryId = 1L;

        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .name("침착맨 짤")
                .boardId(boardId)
                .build();

        given(categoryService.update(any(Long.class), any(CategoryUpdateRequestDto.class)))
                .willReturn(categoryId);

        // when
        // then
        mvc.perform(put("/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().string(categoryId.toString()))
                .andExpect(status().isOk());

    }

    @Test
    public void 카테고리_삭제() throws Exception {
        // given
        Long categoryId = 1L;

        // when
        // then
        mvc.perform(delete("/category/{id}", categoryId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
