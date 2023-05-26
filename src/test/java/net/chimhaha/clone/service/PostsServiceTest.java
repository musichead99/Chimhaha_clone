package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@ExtendWith(MockitoExtension.class) // service 레이어 테스트 시 사용하는 어노테이션
public class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private MenuService menuService;

    @Mock
    private BoardsService boardsService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ImagesService imagesService;

    @InjectMocks
    private PostsService postsService;

    String title = "테스트 게시글";
    String content = "테스트 본문";
    Boolean flag = true;

    @Test
    public void 첨부파일_없는_게시글_등록() {
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

        Posts post = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(dto.getPopularFlag())
                .build();
        /* ReflectionTestUtils는 객체의 private field에 값을 주입할 수 있다. */
        ReflectionTestUtils.setField(post, "id", 1L); // 가짜 게시글 id 주입

        given(postsRepository.save(any(Posts.class)))
                .willReturn(post);
        given(menuService.findById(any(Long.class)))
                .willReturn(menu);
        given(boardsService.findById(any(Long.class)))
                .willReturn(board);
        given(categoryService.findById(any(Long.class)))
                .willReturn(category);

        //when
        PostsSaveResponseDto responseDto = postsService.save(dto);

        //then
        assertAll(
                () -> assertEquals(1L, responseDto.getPostId()),
                () -> assertEquals(0, responseDto.getImageIds().size())
        );
    }

    @Test
    public void 첨부파일_있는_게시글_등록() {
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

        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .menuId(1L)
                .boardId(1L)
                .categoryId(1L)
                .popularFlag(flag)
                .build();

        /* postsService로 넘겨 줄 MultipartFile 리스트 */
        List<MultipartFile> images = new ArrayList<>();

        /* postsService.findPostById()가 반환할 post엔티티 */
        Posts post = Posts.builder()
                .title(title)
                .content(content)
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(flag)
                .build();
        ReflectionTestUtils.setField(post, "id", 1L);

        /* postsService.save()가 반환할 responseDtoDemo */
        PostsSaveResponseDto responseDtoDemo = PostsSaveResponseDto.from(post);

        /* imagesService.save()가 반환할 저장된 이미지의 id 리스트 */
        List<Long> uploadedImageIds = new ArrayList<>();
        uploadedImageIds.add(30L);

        given(postsRepository.save(any(Posts.class)))
                .willReturn(post);
        given(menuService.findById(any(Long.class)))
                .willReturn(menu);
        given(boardsService.findById(any(Long.class)))
                .willReturn(board);
        given(categoryService.findById(any(Long.class)))
                .willReturn(category);
        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.of(post));
        given(imagesService.save(any(Posts.class), anyList()))
                .willReturn(uploadedImageIds);

        // when
        PostsSaveResponseDto responseDto = postsService.save(dto, images);

        // then
        assertAll(
                () -> assertEquals(1L, responseDto.getPostId()),
                () -> assertEquals(1, responseDto.getImageIds().size()),
                () -> assertEquals(30L, responseDto.getImageIds().get(0))
        );

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
    public void 페이징_카테고리별_게시글_조회() {
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

        given(categoryService.findById(any(Long.class)))
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

        given(boardsService.findById(any(Long.class)))
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
                () -> assertEquals(board.getName(),dtoList.getContent().get(0).getBoardName())
        );
    }

    @Test
    public void 페이징_게시글_메뉴별_조회() {
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

        given(menuService.findById(any(Long.class)))
                .willReturn(menu);
        given(postsRepository.findByMenu(any(Menu.class), any(Pageable.class)))
                .willReturn(pagedPosts);

        // when
        Page<PostsFindResponseDto> dtoList = postsService.findByMenu(1L, pageable);

        // then
        assertAll(
                () -> assertEquals(page, dtoList.getNumber()),
                () -> assertEquals(size, dtoList.getSize()),
                () -> assertEquals(amount, dtoList.getNumberOfElements()),
                () -> assertEquals(menu.getName(),dtoList.getContent().get(0).getMenuName())
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
        given(categoryService.findById(any(Long.class)))
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
        postsService.increaseViewCount(1L);

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
