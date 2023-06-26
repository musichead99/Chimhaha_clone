package net.chimhaha.clone.dto.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CommentsUpdateRequestDto {

    @NotNull
    String content;

    @Builder
    public CommentsUpdateRequestDto(String content) {
        this.content = content;
    }
}
