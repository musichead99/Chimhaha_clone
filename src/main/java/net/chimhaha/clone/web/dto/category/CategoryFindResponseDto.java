package net.chimhaha.clone.web.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryFindResponseDto {

    private Long id;
    private String name;
    private Long boardId;
    private String boardName;

    @Builder
    public CategoryFindResponseDto(Long id, String name ,Long boardId, String boardName) {
        this.id = id;
        this.name = name;
        this.boardId = boardId;
        this.boardName = boardName;
    }
}
