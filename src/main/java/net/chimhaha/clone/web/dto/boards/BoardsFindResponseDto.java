package net.chimhaha.clone.web.dto.boards;

import lombok.Getter;
import net.chimhaha.clone.domain.boards.Boards;

@Getter
public class BoardsFindResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer likeLimit;

    public BoardsFindResponseDto(Boards board) {
        this.id = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
        this.likeLimit = board.getLikeLimit();
    }
}
