package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.utils.FileUploadUtils;
import net.chimhaha.clone.dto.images.ImagesSaveResponseDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class ImagesServiceTest {

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private FileUploadUtils fileUploadUtils;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ImagesService imagesService;

    @Test
    public void 이미지_정보_DB에_등록하기() {
        // given
        Member member = mock(Member.class);

        Images image = Images.builder()
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
                "gif",
                new byte[100]
        );
        originalFiles.add(mockMultipartFile);

        /* fileUploadService가 파일 저장 후 반환할 file객체 mocking */
        File mockFile = mock(File.class); // 직접 mock메소드 사용해서 mocking
        given(mockFile.getName()).willReturn("ab6bf24f-1401-42d1-b71c-5353b17300f0_stored_테스트 이미지");

        /* repository 빈들 mocking */
        given(imagesRepository.save(any(Images.class)))
                .willReturn(image);
        given(fileUploadUtils.save(any(MultipartFile.class)))
                .willReturn(mockFile);
        given(memberService.findById(any(Long.class)))
                .willReturn(member);

        // when
        List<ImagesSaveResponseDto> responseDtoList = imagesService.save(originalFiles, 1L);

        // then
        assertAll(
                () -> assertEquals("/images/2", responseDtoList.get(0).getUrl()),
                () -> assertEquals(2, responseDtoList.get(0).getId()),
                () -> assertEquals("테스트 이미지", responseDtoList.get(0).getName()),
                () -> verify(imagesRepository, times(1)).save(any(Images.class)),
                () -> verify(fileUploadUtils, times(1)).save(any(MultipartFile.class)),
                () -> verify(memberService, times(1)).findById(any(Long.class))
        );
    }

    @Test
    public void 이미지_삭제하기() {
        // given
        Posts post = mock(Posts.class);

        Images image = Images.builder()
                .post(post)
                .realFileName("테스트 이미지")
                .storedFileName("ab6bf24f-1401-42d1-b71c-5353b17300f0_stored_테스트 이미지")
                .storedFilePath("C:\\test\\path")
                .storedFileSize(112453)
                .build();

        given(imagesRepository.findById(any(Long.class)))
                .willReturn(Optional.of(image));
        willDoNothing().given(fileUploadUtils).delete(any(File.class));

        // when
        imagesService.delete(1L);

        // then
        assertAll(
                () -> verify(imagesRepository,times(1)).findById(any(Long.class))
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

    @Test
    public void 게시글_등록_시_첨부할_이미지_조회_성공() {
        // given
        List<Long> imageIdList = Arrays.asList(1L, 3L, 5L, 7L);

        List<Images> images = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            Images image = mock(Images.class);
            images.add(image);
        }


        given(imagesRepository.findByIdAndPostIsNullIn(anyList()))
                .willReturn(images);

        // when
        List<Images> imagesToAttach = imagesService.findByIdIn(imageIdList);

        // then
        assertAll(
                () -> assertEquals(4, imagesToAttach.size())
        );

    }

    @Test
    public void 게시글_등록_시_첨부할_이미지_조회_실패() {
        // given
        List<Long> imageIdList = Arrays.asList(1L, 3L, 5L, 7L);

        List<Images> images = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            Images image = mock(Images.class);
            images.add(image);
        }


        given(imagesRepository.findByIdAndPostIsNullIn(anyList()))
                .willReturn(images);

        // when
        // then
        assertAll(
                () -> assertThrows(CustomException.class,() -> {
                    imagesService.findByIdIn(imageIdList);
                })
        );

    }

    @Test
    public void 게시글_수정_시_첨부할_이미지_조회_성공() {
        List<Long> imageIdList = Arrays.asList(1L, 3L, 5L, 7L);

        List<Images> images = new ArrayList<>();

        Posts post = mock(Posts.class);

        for(int i = 0; i < 4; i++) {
            Images image = mock(Images.class);
            images.add(image);
        }


        given(imagesRepository.findByIdAndPostIsNullOrPostIn(anyList(), any(Posts.class)))
                .willReturn(images);

        // when
        List<Images> imagesToAttach = imagesService.findByIdIn(imageIdList, post);

        // then
        assertAll(
                () -> assertEquals(4, imagesToAttach.size())
        );
    }

    @Test
    public void 게시글_수정_시_첨부할_이미지_조회_실패() {
        // given
        List<Long> imageIdList = Arrays.asList(1L, 3L, 5L, 7L);

        List<Images> images = new ArrayList<>();

        Posts post = mock(Posts.class);

        for(int i = 0; i < 3; i++) {
            Images image = mock(Images.class);
            images.add(image);
        }


        given(imagesRepository.findByIdAndPostIsNullOrPostIn(anyList(), any(Posts.class)))
                .willReturn(images);

        // when
        // then
        assertAll(
                () -> assertThrows(CustomException.class,() -> {
                    imagesService.findByIdIn(imageIdList, post);
                })
        );

    }
}