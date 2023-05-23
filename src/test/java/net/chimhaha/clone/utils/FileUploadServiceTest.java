package net.chimhaha.clone.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService fileUploadService;

    private final String DEFAULT_STORE_PATH = "C:\\Users\\CMG16\\Desktop\\coding\\images";

    @Test
    public void 파일_로컬_저장소에_저장하기() throws IOException {
        // given
        String filename = "testGif";
        String contentType = "gif";
        String filepath = "src\\test\\resources\\images\\";
        File mockFile = new File(filepath + filename + "." + contentType);
        FileInputStream fis = new FileInputStream(mockFile);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("images", filename + "." + contentType, contentType, fis);

        ReflectionTestUtils.setField(fileUploadService, "path", DEFAULT_STORE_PATH); // 저장 경로 설정

        // when
        File uploadedFile = fileUploadService.upload(mockMultipartFile);

        uploadedFile.deleteOnExit(); // 테스트 완료 후 jvm이 종료되면 삭제되도록 파일 설정

        // then
        assertAll(
                () -> assertEquals("testGif.gif", uploadedFile.getName().split("_")[1])
        );
    }
}
