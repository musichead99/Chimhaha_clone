package net.chimhaha.clone.web.dto.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class BoardsSaveRequestDto {
    private String name;
    private String description;
    private Integer likeLimit;

    @Builder
    public BoardsSaveRequestDto(String name, String description, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }
}
