package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.MenuService;
import net.chimhaha.clone.web.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.web.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.web.dto.menu.MenuUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menu")
    public Long create(@RequestBody MenuSaveRequestDto dto) {
        return menuService.create(dto);
    }

    @GetMapping("/menu")
    public List<MenuFindResponseDto> find() {
        return menuService.find();
    }

    @PutMapping("/menu/{id}")
    public Long update(@PathVariable Long id, @RequestBody MenuUpdateRequestDto dto) {
        return menuService.update(id, dto);
    }

    @DeleteMapping("menu/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
