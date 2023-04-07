package net.chimhaha.clone.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {
    private String name;

    @Builder
    public MenuUpdateRequestDto(String name) {
        this.name = name;
    }
}
