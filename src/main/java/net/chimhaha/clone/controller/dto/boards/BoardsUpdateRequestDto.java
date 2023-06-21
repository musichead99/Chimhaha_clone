package net.chimhaha.clone.controller.dto.boards;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class BoardsUpdateRequestDto {

    @NotNull
    private final String name;

    @NotNull
    private final String description;

    @Min(value = 10)
    @NotNull
    private final Integer likeLimit;

    @Builder
    public BoardsUpdateRequestDto(String name, String description, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }
}
