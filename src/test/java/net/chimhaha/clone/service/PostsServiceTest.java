package net.chimhaha.clone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.PostsFindResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@ExtendWith(MockitoExtension.class) // service 레이어 테스트 시 사용하는 어노테이션
public class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private BoardsRepository boardsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostsService postsService;

    String title = "테스트 게시글";
    String content = "테스트 본문";
    String subject = "침착맨";
    Boolean flag = true;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 게시글_등록() {
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

        Posts posts = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(dto.getPopularFlag())
                .build();
        /* ReflectionTestUtils는 객체의 private field에 값을 주입할 수 있다. */
        ReflectionTestUtils.setField(posts, "id", 1L); // 가짜 게시글 id 주입

        given(postsRepository.save(any(Posts.class)))
                .willReturn(posts);
        given(menuRepository.getReferenceById(any(Long.class)))
                .willReturn(menu);
        given(boardsRepository.getReferenceById(any(Long.class)))
                .willReturn(board);
        given(categoryRepository.getReferenceById(any(Long.class)))
                .willReturn(category);

        //when
        Long createdPostsId = postsService.save(dto);

        //then
        assertEquals(1L, createdPostsId);
    }

    @Test
    public void 페이징_게시글_전체_조회() {
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

        List<Posts> posts = new LinkedList<>();
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
            posts.add(post);
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        Page<Posts> pagedPosts = new PageImpl<>(posts, pageable, posts.size());

        given(postsRepository.findAll(any(Pageable.class)))
                .willReturn(pagedPosts);

        // when
        Page<PostsFindResponseDto> dtoList = postsService.find(pageable);

        // then
        assertAll(
                () -> assertEquals(page, dtoList.getNumber()),
                () -> assertEquals(size, dtoList.getSize()),
                () -> assertEquals(amount, dtoList.getNumberOfElements()),
                () -> assertEquals(1,dtoList.getContent().get(0).getId())
        );
    }

    @Test
    public void 카테고리별_게시글_조회() {
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

        List<Posts> posts = new LinkedList<>();
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
            posts.add(post);
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        Page<Posts> pagedPosts = new PageImpl<>(posts, pageable, posts.size());

        given(postsRepository.findByCategory(any(Category.class), any(Pageable.class)))
                .willReturn(pagedPosts);

        given(categoryRepository.getReferenceById(any(Long.class)))
                .willReturn(category);

        //when
        Page<PostsFindResponseDto> dtoList = postsService.findByCategory(1L, pageable);

        //then
        assertAll(
                () -> assertEquals(page, dtoList.getNumber()),
                () -> assertEquals(size, dtoList.getSize()),
                () -> assertEquals(amount, dtoList.getNumberOfElements()),
                () -> assertEquals(1,dtoList.getContent().get(0).getId())
        );
    }

    @Test
    public void 페이징_게시판별_게시글_조회() {
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

        List<Posts> posts = new LinkedList<>();
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
            posts.add(post);
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> pagedPosts = new PageImpl<>(posts, pageable, posts.size());

        given(boardsRepository.getReferenceById(any(Long.class)))
                .willReturn(board);
        given(postsRepository.findByBoard(any(Boards.class), any(Pageable.class)))
                .willReturn(pagedPosts);

        // when
        Page<PostsFindResponseDto> dtoList = postsService.findByBoard(1L, pageable);

        // then
        assertAll(
                () -> assertEquals(page, dtoList.getNumber()),
                () -> assertEquals(size, dtoList.getSize()),
                () -> assertEquals(amount, dtoList.getNumberOfElements()),
                () -> assertEquals(board.getName(),dtoList.getContent().get(0).getBoard())
        );
    }

    @Test
    public void 게시글_상세_조회() {
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

        Long fakePostsId = 1L;
        ReflectionTestUtils.setField(post, "id", fakePostsId);
        ReflectionTestUtils.setField(post, "views", 0);
        PostsFindByIdResponseDto expectedDto = PostsFindByIdResponseDto.from(post);
        
        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(post));
        //when
        Long postsId = 1L;
        PostsFindByIdResponseDto dto = postsService.findById(postsId);

        //then
        assertEquals(expectedDto.getId(), dto.getId());
    }

    @Test
    public void 게시글_수정() {
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
        Long postsId = 1L;
        ReflectionTestUtils.setField(posts, "id", postsId);
        ReflectionTestUtils.setField(posts, "views", 0);

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title(title)
                .content("테스트 본문 2")
                .categoryId(1L)
                .popularFlag(flag)
                .build();

        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(posts));
        given(categoryRepository.getReferenceById(any(Long.class)))
                .willReturn(category);

        // when
        Long updatedId = postsService.update(postsId, dto);

        // then
        assertAll(() -> assertEquals(postsId, updatedId),
                () -> assertEquals("테스트 본문 2", posts.getContent()));
    }

    @Test
    public void 조회수_증가() {
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

        int defaultViews = 0;
        ReflectionTestUtils.setField(posts, "id", 1L);
        ReflectionTestUtils.setField(posts, "views", defaultViews);

        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(posts));

        //when
        postsService.increaseViewCount(1l);

        //then
        assertEquals(defaultViews + 1, posts.getViews());
    }

    @Test
    public void 게시글_삭제() {
        // given
        Long postsId = 1L;

        // when
        postsService.delete(postsId);

        // then
        /* postsService.delete()가 반환값이 없으므로 delete()내부의 postsRepository가 제대로 실행되었는지를 검증한다
        *  verify를 통해서 deletebyId가 예상대로 1번 호출되었는지를 검증한다 */
        verify(postsRepository, times(1)).deleteById(postsId);
    }
}
