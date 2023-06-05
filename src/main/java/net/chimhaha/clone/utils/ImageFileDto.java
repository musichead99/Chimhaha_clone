package net.chimhaha.clone.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Builder
@AllArgsConstructor
@Getter
public class ImageFileDto {
    private byte[] bytes;
    private MediaType mediaType;
}