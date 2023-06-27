package net.chimhaha.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.config.SecurityConfig;
import net.chimhaha.clone.config.jwt.JwtAuthenticationFilter;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.service.MenuService;
import net.chimhaha.clone.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.dto.menu.MenuUpdateRequestDto;
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
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = MenuController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthenticationFilter.class})
})
public class MenuControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MenuService menuService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 메뉴_등록() throws Exception {
        // given
        MenuSaveRequestDto dto = MenuSaveRequestDto.builder()
                .name("침착맨")
                .build();

        given(menuService.save(any(MenuSaveRequestDto.class)))
                .willReturn(1L);

        // when
        // then
        mvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().string("1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void 메뉴_전체_조회() throws Exception {
        // given
        String[] list = {"침하하", "전체글", "침착맨", "웃음", "운동"};
        List<Menu> menuList = new LinkedList<>();

        for(int i = 0; i < 5; i++) {
            menuList.add(Menu.builder()
                    .name(list[i])
                    .build());
        }
        List<MenuFindResponseDto> dtoList = menuList.stream()
                        .map(MenuFindResponseDto::from)
                        .collect(Collectors.toList());

        given(menuService.find())
                .willReturn(dtoList);

        // when
        // then
        mvc.perform(get("/menu")
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
                .andExpect(status().isOk());
    }

    @Test
    public void 메뉴_수정() throws Exception {
        // given
        MenuUpdateRequestDto dto = MenuUpdateRequestDto.builder()
                .name("침하하")
                .build();

        given(menuService.update(any(Long.class), any(MenuUpdateRequestDto.class)))
                .willReturn(1L);

        // when
        // then
        mvc.perform(put("/menu/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(content().string("1"))
                .andExpect(status().isOk());
    }

    @Test
    public void 메뉴_삭제() throws Exception {
        // given

        willDoNothing().given(menuService).delete(any(Long.class));

        // when
        // then
        mvc.perform(delete("/menu/{id}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
