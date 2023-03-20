package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    String title;
    String content;
    String category;
    Short popularFlag;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String category, Short popularFlag) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.popularFlag = popularFlag;
    }
}
