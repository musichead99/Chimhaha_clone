package net.chimhaha.clone.controller.dto.images;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.images.Images;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImagesSaveResponseDto {
    private final Long id;
    private final String name;
    private final String url;

    public static ImagesSaveResponseDto from(Images image) {
        String url = "/images/" + image.getId();

        return new ImagesSaveResponseDto(
                image.getId(),
                image.getRealFileName(),
                url
        );
    }
}
