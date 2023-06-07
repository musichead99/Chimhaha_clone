package net.chimhaha.clone.web.dto.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class BoardsSaveRequestDto {
    @NotNull
    private Long menuId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Min(value = 10)
    @NotNull
    private Integer likeLimit;

    @Builder
    public BoardsSaveRequestDto(Long menuId, String name, String description, Integer likeLimit) {
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }
}
