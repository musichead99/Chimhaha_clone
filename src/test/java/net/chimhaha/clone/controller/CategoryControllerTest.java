package net.chimhaha.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.config.SecurityConfig;
import net.chimhaha.clone.config.auth.CustomOAuth2User;
import net.chimhaha.clone.config.jwt.JwtAuthenticationFilter;
import net.chimhaha.clone.service.CategoryService;
import net.chimhaha.clone.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.dto.category.CategoryUpdateRequestDto;
import net.chimhaha.clone.service.CommunityService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthenticationFilter.class})
})
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommunityService communityService;

    String name = "침착맨";
    String boardName = "침착맨";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 카테고리_등록() throws Exception {
        // given
        CustomOAuth2User customOAuth2User = mock(CustomOAuth2User.class);
        
        Long boardId = 1L;
        Long categoryId = 1L;

        CategorySaveRequestDto dto = CategorySaveRequestDto.builder()
                .name(name)
                .boardId(boardId)
                .build();

        given(communityService.saveCategory(any(CategorySaveRequestDto.class), any(Long.class)))
                .willReturn(categoryId);
        given(customOAuth2User.getId())
                .willReturn(1L);
        given(customOAuth2User.getName())
                .willReturn("침착맨");
        
        
        // when
        // then
        mvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(oauth2Login()
                                .oauth2User(customOAuth2User)))
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

        given(communityService.findCategory())
                .willReturn(dtoList);

        // when
        // then
        mvc.perform(get("/category")
                        .with(csrf())
                        .with(oauth2Login()))
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

        given(communityService.updateCategory(any(Long.class), any(CategoryUpdateRequestDto.class)))
                .willReturn(categoryId);

        // when
        // then
        mvc.perform(put("/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(oauth2Login()))
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
                        .with(csrf())
                        .with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
