package net.chimhaha.clone.controller.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CategorySaveRequestDto {

    @NotNull
    private String name;

    @NotNull
    private Long boardId;

    @Builder
    public CategorySaveRequestDto(String name, Long boardId) {
        this.name = name;
        this.boardId = boardId;
    }
}
