package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 게시글을 작성할 때 전달받는 dto */
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String subject;
    private Short popularFlag;

    @Builder
    public PostsSaveRequestDto(String title, String content, String subject, Short popularFlag) {
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.popularFlag = popularFlag;
    }

}
