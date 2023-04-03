package net.chimhaha.clone.web.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequestDto {
    private String name;
    private Long boardId;

    @Builder
    public CategoryUpdateRequestDto(String name, Long boardId) {
        this.name = name;
        this.boardId = boardId;
    }
}
