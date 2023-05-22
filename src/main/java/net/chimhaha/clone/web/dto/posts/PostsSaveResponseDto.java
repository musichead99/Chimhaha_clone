package net.chimhaha.clone.web.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsSaveResponseDto {
    Long postId;
    @Builder.Default
    List<Long> imageId = new ArrayList<>();
    @Builder.Default
    int requestCount = 0;
    @Builder.Default
    int responseCount = 0;
}
