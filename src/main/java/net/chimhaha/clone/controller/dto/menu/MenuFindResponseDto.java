package net.chimhaha.clone.controller.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.controller.dto.boards.BoardsFindResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MenuFindResponseDto {
    private Long id;
    private String name;
    private List<BoardsFindResponseDto> boards;

    public static MenuFindResponseDto from(Menu menu) {
        List<BoardsFindResponseDto> list = menu.getBoards().stream()
                .map(BoardsFindResponseDto::from)
                .collect(Collectors.toList());

        return new MenuFindResponseDto(menu.getId(), menu.getName(), list);
    }
}
