package net.chimhaha.clone.web.dto.boards;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardsUpdateRequestDto {
    private final String name;
    private final String description;
    private final Integer likeLimit;

    @Builder
    public BoardsUpdateRequestDto(String name, String description, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }
}
