package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    String title;
    String content;
    String subject;
    Boolean popularFlag;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String subject, Boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.popularFlag = popularFlag;
    }
}
