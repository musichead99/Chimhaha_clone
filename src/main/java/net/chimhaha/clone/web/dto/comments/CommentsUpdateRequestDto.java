package net.chimhaha.clone.web.dto.comments;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentsUpdateRequestDto {

    String content;

    @Builder
    public CommentsUpdateRequestDto(String content) {
        this.content = content;
    }
}
