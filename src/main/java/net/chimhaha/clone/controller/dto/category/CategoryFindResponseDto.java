package net.chimhaha.clone.controller.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.chimhaha.clone.domain.category.Category;


@Getter
@AllArgsConstructor()
public class CategoryFindResponseDto {

    private Long id;
    private String name;
    private Long boardId;
    private String boardName;

    public static CategoryFindResponseDto from(Category category) {
        return new CategoryFindResponseDto(
                category.getId(),
                category.getName(),
                category.getBoard().getId(),
                category.getBoard().getName()
        );
    }
}
