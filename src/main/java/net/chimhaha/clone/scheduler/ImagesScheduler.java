package net.chimhaha.clone.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.service.ImagesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImagesScheduler {

    private final ImagesService imagesService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deletesImagesThatHaveNoRelationship() {
        List<Images> garbage = imagesService.findByPostIsNull();

        log.info("job start.............................");

        garbage.stream()
                .map(Images::getId)
                .forEach(imagesService::delete);

        log.info("job done..............................\n");
    }
}
