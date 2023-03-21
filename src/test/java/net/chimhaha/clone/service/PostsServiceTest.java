package net.chimhaha.clone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.posts.PostsFindByCategoryResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // service 레이어 테스트 시 사용하는 어노테이션
public class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private PostsService postsService;

    String title = "테스트 게시글";
    String content = "테스트 본문";
    String category = "침착맨";
    Short flag = 1;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 게시글_저장() {
        //given
        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build();
        Posts posts = Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .popularFlag(dto.getPopularFlag())
                .build();

        Long fakePostsId = 1L;
        /* ReflectionTestUtils는 객체의 private field에 값을 주입할 수 있다. */
        ReflectionTestUtils.setField(posts, "id", fakePostsId); // 가짜 게시글 id 주입

        given(postsRepository.save(any(Posts.class)))
                .willReturn(posts);

        //when
        Long createdPostsId = postsService.save(dto);

        //then
        assertEquals(createdPostsId, fakePostsId);
    }

    @Test
    public void 카테고리별_Posts조회() {
        // given
        List<PostsFindByCategoryResponseDto> expectedPostsResponseList = new LinkedList<>(); // service 계층에서 반환될 리스트 예상
        List<Posts> postsList = new LinkedList<>(); // repository가 반환할 리스트

        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build();

        ReflectionTestUtils.setField(posts, "id", 1L);
        expectedPostsResponseList.add(new PostsFindByCategoryResponseDto(posts));
        postsList.add(posts);

        given(postsRepository.findByCategory(any(String.class)))
                .willReturn(postsList);

        //when
        List<PostsFindByCategoryResponseDto> postsResponseList = postsService.findByCategory("침착맨");

        //then
        assertEquals(postsResponseList.get(0).getCategory(), expectedPostsResponseList.get(0).getCategory());
    }

    @Test
    public void 게시글_id로_조회() {
        //given
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build();

        Long fakePostsId = 1L;
        ReflectionTestUtils.setField(posts, "id", fakePostsId);
        ReflectionTestUtils.setField(posts, "views", 0);
        PostsFindByIdResponseDto expectedDto = new PostsFindByIdResponseDto(posts);
        
        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(posts));
        //when
        Long postsId = 1L;
        PostsFindByIdResponseDto dto = postsService.findById(postsId);

        //then
        assertEquals(expectedDto.getId(), dto.getId());
    }

    @Test
    public void 게시글_수정() {
        // given
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .category(category)
                .popularFlag(flag)
                .build();
        Long postsId = 1L;
        ReflectionTestUtils.setField(posts, "id", postsId);
        ReflectionTestUtils.setField(posts, "views", 0);

        String updatedContent = "테스트 본문 2"; // 수정된 본문
        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(updatedContent)
                .category(category)
                .popularFlag(flag)
                .build();

        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(posts));

        // when
        Long updatedId = postsService.update(postsId, dto);

        // then
        assertAll(() -> assertEquals(postsId, updatedId),
                () -> assertEquals(updatedContent, posts.getContent()));
    }

    @Test
    public void 조회수_증가() {
        //given
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
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
