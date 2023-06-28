package net.chimhaha.clone.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {

    @NotNull
    private String name;

    @Builder
    public MenuUpdateRequestDto(String name) {
        this.name = name;
    }
}
