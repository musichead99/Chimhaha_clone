package net.chimhaha.clone.controller.dto.boards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.chimhaha.clone.domain.boards.Boards;

@AllArgsConstructor
@Getter
public class BoardsFindResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer likeLimit;

    public static BoardsFindResponseDto from(Boards board) {
        return new BoardsFindResponseDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getLikeLimit()
        );
    }
}
