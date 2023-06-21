package net.chimhaha.clone.controller.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class MenuSaveRequestDto {

    @NotNull
    private String name;

    @Builder
    public MenuSaveRequestDto(String name) {
        this.name = name;
    }
}
