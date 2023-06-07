package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.MenuService;
import net.chimhaha.clone.web.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.web.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.web.dto.menu.MenuUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menu")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long save(@Valid @RequestBody MenuSaveRequestDto dto) {
        return menuService.save(dto);
    }

    @GetMapping("/menu")
    public List<MenuFindResponseDto> find() {
        return menuService.find();
    }

    @PutMapping("/menu/{id}")
    public Long update(@PathVariable("id") Long id, @Valid @RequestBody MenuUpdateRequestDto dto) {
        return menuService.update(id, dto);
    }

    @DeleteMapping("menu/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        menuService.delete(id);
    }
}
