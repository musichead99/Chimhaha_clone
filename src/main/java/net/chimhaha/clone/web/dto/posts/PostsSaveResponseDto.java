package net.chimhaha.clone.web.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsSaveResponseDto {
    Long postId;
    @Builder.Default
    List<Long> imageIds = new ArrayList<>();
    @Builder.Default
    int requestCount = 0;
    @Builder.Default
    int uploadedCount = 0;

    public static PostsSaveResponseDto from(Posts post) {
        return PostsSaveResponseDto.builder()
                .postId(post.getId())
                .build();
    }

    public void setValues(List<Long> imageIds, int requestCount, int uploadedCount) {
        this.imageIds = imageIds;
        this.requestCount = requestCount;
        this.uploadedCount = uploadedCount;
    }
}
