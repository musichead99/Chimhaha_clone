package net.chimhaha.clone.web.dto.boards;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardsUpdateRequestDto {
    String name;
    String description;
    Integer likeLimit;

    @Builder
    public BoardsUpdateRequestDto(String name, String description, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }
}
