package net.chimhaha.clone.web.dto.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsSaveRequestDto {
    String content;
    Long parentId;
    Long postId;

    @Builder
    public CommentsSaveRequestDto(String content, Long parentId, Long postId) {
        this.content = content;
        this.parentId = parentId;
        this.postId = postId;
    }
}
