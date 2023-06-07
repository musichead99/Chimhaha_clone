package net.chimhaha.clone.web.dto.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CommentsSaveRequestDto {

    @NotNull
    String content;

    Long parentId;

    @NotNull
    Long postId;

    @Builder
    public CommentsSaveRequestDto(String content, Long parentId, Long postId) {
        this.content = content;
        this.parentId = parentId;
        this.postId = postId;
    }
}
