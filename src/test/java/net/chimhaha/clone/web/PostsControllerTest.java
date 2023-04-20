package net.chimhaha.clone.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.service.PostsService;
import net.chimhaha.clone.web.dto.posts.PostsFindResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
/* @SpringBootTest는 모든 빈을 로드하기 때문에 Controller계층만 테스트할 때는 @WebMvcTest를 사용 */
@WebMvcTest(controllers = PostsController.class)
public class PostsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostsService postsService;

    String title = "테스트 게시글";
    String content = "테스트 본문";
    Boolean flag = true;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void  게시글_등록() throws Exception {
        //given
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

        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .menuId(1L)
                .boardId(1L)
                .categoryId(1L)
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
    public void 페이징_게시글_전체_조회() throws Exception {
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

        List<PostsFindResponseDto> dtoList = new LinkedList<>();
        
        int amount = 5;
        for(int i = 0; i < amount; i++) {

            Posts post = Posts.builder()
                    .title(title)
                    .content(content)
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(flag)
                    .build();

            ReflectionTestUtils.setField(post,"id", i + 1L);
            dtoList.add(PostsFindResponseDto.from(post));
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page,size);

        Page<PostsFindResponseDto> pagedDtoList = new PageImpl<>(dtoList, pageable, dtoList.size());

        given(postsService.find(any(Pageable.class)))
                .willReturn(pagedDtoList);

        // when
        // then
        mvc.perform(get("/posts")
                .param("page", "0"))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedDtoList)))
                .andExpect(status().isOk());
    }

    @Test
    public void 카테고리별_게시글_조회() throws Exception {
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

        List<PostsFindResponseDto> dtoList = new LinkedList<>();

        int amount = 5;
        for(int i = 0; i < amount; i++) {

            Posts post = Posts.builder()
                    .title(title)
                    .content(content)
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(flag)
                    .build();

            ReflectionTestUtils.setField(post,"id", i + 1L);
            dtoList.add(PostsFindResponseDto.from(post));
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page,size);

        Page<PostsFindResponseDto> pagedDtoList = new PageImpl<>(dtoList, pageable, dtoList.size());

        given(postsService.findByCategory(any(Long.class), any(Pageable.class))).willReturn(pagedDtoList);

        //when
        //then
        mvc.perform(get("/posts")
                        .param("category", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedDtoList)));
    }

    @Test
    public void 페이징_게시판별_게시글_조회() throws Exception {
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

        List<PostsFindResponseDto> dtoList = new LinkedList<>();
        int amount = 5;
        for(int i = 0; i < amount; i++) {

            Posts post = Posts.builder()
                    .title(title)
                    .content(content)
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(flag)
                    .build();

            ReflectionTestUtils.setField(post,"id", i + 1L);
            dtoList.add(PostsFindResponseDto.from(post));
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<PostsFindResponseDto> pagedDtoList = new PageImpl<>(dtoList, pageable, dtoList.size());

        given(postsService.findByBoard(any(Long.class), any(Pageable.class)))
                .willReturn(pagedDtoList);

        // when
        // then
        mvc.perform(get("/posts")
                        .param("board", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedDtoList)));

    }

    @Test
    public void 게시글_상세_조회() throws Exception {
        //given
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
                .title(title)
                .content(content)
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(flag)
                .build();

        PostsFindByIdResponseDto dto = PostsFindByIdResponseDto.from(post);
        given(postsService.findById(any())).willReturn(dto);

        //when
        //then
        mvc.perform(get("/posts/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    public void 게시글_수정() throws Exception {
        //given
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

        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(flag)
                .build();
        Long postId = 1l;
        ReflectionTestUtils.setField(posts, "id", postId);
        ReflectionTestUtils.setField(posts, "views", 0);

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title(title)
                .content("테스트 본문 2")
                .categoryId(2L)
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

        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .menu(menu)
                .board(board)
                .category(category)
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
