package net.chimhaha.clone.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequestDto {

    @NotNull
    private String name;

    @NotNull
    private Long boardId;

    @Builder
    public CategoryUpdateRequestDto(String name, Long boardId) {
        this.name = name;
        this.boardId = boardId;
    }
}
