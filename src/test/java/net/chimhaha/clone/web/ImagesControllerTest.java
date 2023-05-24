package net.chimhaha.clone.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chimhaha.clone.service.ImagesService;
import net.chimhaha.clone.utils.FileUploadService;
import net.chimhaha.clone.utils.ImageFileDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = ImagesController.class)
public class ImagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImagesService imagesService;

    @MockBean
    private FileUploadService fileUploadService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
}
