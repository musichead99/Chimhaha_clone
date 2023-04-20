package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    String title;
    String content;
    Long categoryId;
    Boolean popularFlag;

    @Builder
    public PostsUpdateRequestDto(String title, String content, Long categoryId, Boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.popularFlag = popularFlag;
    }
}
