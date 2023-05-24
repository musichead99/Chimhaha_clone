package net.chimhaha.clone.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MenuSaveRequestDto {

    private String name;

    @Builder
    public MenuSaveRequestDto(String name) {
        this.name = name;
    }
}
