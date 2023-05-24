package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class ImagesServiceTest {

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private ImagesService imagesService;

    @Test
    public void 이미지_정보_DB에_등록하기() {
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
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .build();
        ReflectionTestUtils.setField(post, "id", 1L);

        Images image = Images.builder()
                .post(post)
                .realFileName("테스트 이미지")
                .storedFileName("stored_테스트 이미지")
                .storedFilePath("C:\\test")
                .storedFileSize(3127)
                .build();
        ReflectionTestUtils.setField(image, "id", 2L);

        /* 파라미터로 전달할 multipart mock file List 작성 */
        List<MultipartFile> originalFiles = new ArrayList<>();

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "images",
                "테스트 이미지",
                "gif", new byte[100]
        );

        originalFiles.add(mockMultipartFile);

        /* 파라미터로 전달할 mock file list 작성 */
        List<File> uploadedFiles = new ArrayList<>();

        File mockFile = mock(File.class); // 직접 mock메소드 사용해서 mocking
        given(mockFile.getName()).willReturn("stored_테스트 이미지");

        uploadedFiles.add(mockFile);

        /* repository 빈들 mocking */
        given(postsRepository.findById(any(Long.class)))
                .willReturn(Optional.of(post));
        given(imagesRepository.save(any(Images.class)))
                .willReturn(image);

        // when
        List<Long> uploadedFilesId = imagesService.save(post.getId(), uploadedFiles, originalFiles);

        // then
        assertAll(
                () -> assertEquals(1, uploadedFilesId.size()),
                () -> assertEquals(2, uploadedFilesId.get(0)),
                () -> verify(postsRepository, times(1)).findById(any(Long.class)),
                () -> verify(imagesRepository, times(1)).save(any(Images.class))
        );
    }

    @Test
    public void 로컬에_저장된_이미지_경로_조회하기() {
        // given
        Posts post = mock(Posts.class);

        Images image = Images.builder()
                .post(post)
                .realFileName("테스트 이미지")
                .storedFileName("stored_테스트 이미지")
                .storedFilePath("C:\\test")
                .storedFileSize(3127)
                .build();
        ReflectionTestUtils.setField(image, "id", 1L);

        given(imagesRepository.findById(any(Long.class)))
                .willReturn(Optional.of(image));

        // when
        String path = imagesService.findImagePathById(1L);

        // then
        assertEquals("C:\\test", path);

    }
}