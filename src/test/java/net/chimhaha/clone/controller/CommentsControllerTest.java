package net.chimhaha.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.config.SecurityConfig;
import net.chimhaha.clone.config.auth.CustomOAuth2User;
import net.chimhaha.clone.config.jwt.JwtAuthenticationFilter;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.service.CommentsService;
import net.chimhaha.clone.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.dto.comments.CommentsUpdateRequestDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = CommentsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthenticationFilter.class})
})
public class CommentsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommunityService communityService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 댓글_등록하기() throws Exception {
        // given
        CustomOAuth2User customOAuth2User = mock(CustomOAuth2User.class);

        CommentsSaveRequestDto dto = CommentsSaveRequestDto.builder()
                .postId(1L)
                .content("테스트 댓글")
                .parentId(null)
                .build();

        given(communityService.saveComment(any(CommentsSaveRequestDto.class), any(Long.class)))
                .willReturn(1L);
        given(customOAuth2User.getId())
                .willReturn(1L);
        given(customOAuth2User.getName())
                .willReturn("침착맨");

        // when
        // then
        mvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(oauth2Login()
                                .oauth2User(customOAuth2User)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    public void 댓글_조회하기() throws Exception {
        // given
        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        Boards board = Boards.builder()
                .menu(menu)
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();
        ReflectionTestUtils.setField(board, "id", 1L);

        Category category = Category.builder()
                .board(board)
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(category, "id", 1L);

        Posts post = Posts.builder()
                .title("테스트 글")
                .content("테스트 내용")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build();
        ReflectionTestUtils.setField(post,"id", 1L);

        List<CommentsFindByPostResponseDto> dtoList = new ArrayList<>();

        Comments comment = Comments.builder()
                .post(post)
                .content("테스트 댓글")
                .parent(null)
                .build();
        ReflectionTestUtils.setField(comment,"id", 1L);

        dtoList.add(CommentsFindByPostResponseDto.from(comment));

        Pageable pageable = PageRequest.of(0, 20);
        Page<CommentsFindByPostResponseDto> pagedDtoList = new PageImpl<>(dtoList, pageable, dtoList.size());

        given(communityService.findCommentsByPost(any(Long.class), any(Pageable.class)))
                .willReturn(pagedDtoList);

        // when
        // then
        mvc.perform(get("/comments")
                        .param("post", "1")
                        .with(csrf())
                        .with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedDtoList)));
    }

    @Test
    public void 댓글_수정하기() throws Exception {
        // given
        CommentsUpdateRequestDto dto = CommentsUpdateRequestDto.builder()
                .content("테스트 댓글 수정")
                .build();

        given(communityService.updateComment(any(Long.class), any(CommentsUpdateRequestDto.class)))
                .willReturn(1L);

        // when
        // then
        mvc.perform(put("/comments/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                        .with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void 댓글_삭제하기() throws Exception {
        // given
        willDoNothing().given(communityService).deleteComment(any(Long.class));

        // when
        // then
        mvc.perform(delete("/comments/{id}", "1")
                        .with(csrf())
                        .with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
