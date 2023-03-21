package net.chimhaha.clone.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.service.PostsService;
import net.chimhaha.clone.web.dto.posts.PostsFindBySubjectResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/* @SpringBootTest는 모든 빈을 로드하기 때문에 Controller계층만 테스트할 때는 @WebMvcTest를 사용 */
@WebMvcTest(controllers = PostsController.class)
public class PostsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostsService postsService;

    String title = "테스트 게시글";
    String content = "테스트 본문";
    String subject = "침착맨";
    Short flag = 1;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void Posts_등록() throws Exception {
        //given
        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .subject(subject)
                .popularFlag(flag)
                .build();

        given(postsService.save(any())).willReturn(1L); // mockbean이 어떠한 행동을 취하면 어떠한 결과를 반환한다는 것을 정의

        //when
        //then
        mvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()) // 상태 코드 201(created)반환
                .andExpect(content().string("1")); // 결과값으로 생성한 게시글의 id반환
    }

    @Test
    public void 말머리별_Posts조회() throws Exception {
        // given
        List<PostsFindBySubjectResponseDto> postsList = new LinkedList<>();
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .subject(subject)
                .popularFlag(flag)
                .build();

        for(int i = 0; i < 5; i++) {
            postsList.add(new PostsFindBySubjectResponseDto(posts));
        }

        given(postsService.findBySubject(any())).willReturn(postsList);

        //when
        //then
        mvc.perform(get("/posts?category=침착맨"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(postsList)));
    }

    @Test
    public void 게시글_id로_조회() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .subject(subject)
                .popularFlag(flag)
                .build();

        PostsFindByIdResponseDto dto = new PostsFindByIdResponseDto(posts);
        given(postsService.findById(any())).willReturn(dto);

        //when
        //then
        mvc.perform(get("/posts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    public void 게시글_수정() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .subject(subject)
                .popularFlag(flag)
                .build();
        Long postId = 1l;
        ReflectionTestUtils.setField(posts, "id", postId);
        ReflectionTestUtils.setField(posts, "views", 0);

        String updatedContent = "태스트 본문 2";
        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(updatedContent)
                .subject(subject)
                .popularFlag(flag)
                .build();

        given(postsService.update(any(Long.class), any(PostsUpdateRequestDto.class)))
                .willReturn(postId);
        //when
        //then
        mvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(postId.toString()));
    }

    @Test
    public void 게시글_삭제() throws Exception {
        // given
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .subject(subject)
                .popularFlag(flag)
                .build();
        Long postId = 1l;
        ReflectionTestUtils.setField(posts, "id", postId);
        ReflectionTestUtils.setField(posts, "views", 0);

        doNothing().when(postsService).delete(any(Long.class));
        // when
        // then
        mvc.perform(delete("/posts/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
