package net.chimhaha.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.service.ImagesService;
import net.chimhaha.clone.utils.FileUploadService;
import net.chimhaha.clone.utils.ImageFileDto;
import net.chimhaha.clone.controller.dto.images.ImagesSaveResponseDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = ImagesController.class)
public class ImagesControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImagesService imagesService;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    public void 이미지_불러오기() throws Exception {
        // given
        ImageFileDto dto = ImageFileDto.builder()
                .mediaType(MediaType.IMAGE_GIF)
                .bytes("테스트 이미지".getBytes())
                .build();

        given(imagesService.findImagePathById(any(Long.class)))
                .willReturn("test path");
        given(fileUploadService.getImageFile(any(String.class)))
                .willReturn(dto);

        // when
        // then
        mvc.perform(get("/images/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_GIF_VALUE))
                .andExpect(content().bytes("테스트 이미지".getBytes()));
    }

    @Test
    public void 이미지_등록하기() throws Exception {
        // given
        String filename = "testGif";
        String contentType = "gif";
        String filepath = "src\\test\\resources\\images\\";
        File mockFile = new File(filepath + filename + "." + contentType);
        FileInputStream fis = new FileInputStream(mockFile);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("images", filename + "." + contentType, contentType, fis);

        Images mockImage = mock(Images.class);
        given(mockImage.getId()).willReturn(1L);
        given(mockImage.getRealFileName()).willReturn(filename + "." + contentType);

        ImagesSaveResponseDto responseDto = ImagesSaveResponseDto.from(mockImage);
        List<ImagesSaveResponseDto> dtoList = new ArrayList<>();
        dtoList.add(responseDto);

        given(imagesService.save(anyList()))
                .willReturn(dtoList);

        // when
        // then
        mvc.perform(multipart("/images")
                .file(mockMultipartFile))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)));
    }

    @Test
    public void 이미지_삭제하기() throws Exception {
        // given

        willDoNothing().given(imagesService).delete(any(Long.class));

        // when
        // then
        mvc.perform(delete("/images/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
