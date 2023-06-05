package net.chimhaha.clone.scheduler;

import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.images.ImagesRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@ExtendWith(MockitoExtension.class)
public class ImagesSchedulerTest {

    @Mock
    ImagesRepository imagesService;


    @InjectMocks
    ImagesScheduler imagesScheduler;

    @Test
    public void 게시글에_등록되지_않은_이미지_삭제하기() {
        // given
        List<Images> garbage = new ArrayList<>();

        for(int i = 0; i < 5 ; i++) {
            Images image = mock(Images.class);
            given(image.getStoredFilePath()).willReturn("C:\\test\\path");

            garbage.add(image);
        }

        willDoNothing().given(imagesService).delete(any(Images.class));
        given(imagesService.findByPostIsNull()).willReturn(garbage);

        // when
        imagesScheduler.deletesImagesThatHaveNoRelationship();

        // then
        assertAll(
                () -> verify(imagesService, times(5)).delete(any(Images.class))
        );
    }
}
