package net.chimhaha.clone.controller;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.config.auth.CustomOAuth2User;
import net.chimhaha.clone.service.CommunityService;
import net.chimhaha.clone.dto.menu.MenuFindResponseDto;
import net.chimhaha.clone.dto.menu.MenuSaveRequestDto;
import net.chimhaha.clone.dto.menu.MenuUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final CommunityService communityService;

    @PostMapping("/menu")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long save(@Valid @RequestBody MenuSaveRequestDto dto,
                     @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return communityService.saveMenu(dto, oAuth2User.getId());
    }

    @GetMapping("/menu")
    public List<MenuFindResponseDto> find() {
        return communityService.findMenu();
    }

    @PutMapping("/menu/{id}")
    public Long update(@PathVariable("id") Long id, @Valid @RequestBody MenuUpdateRequestDto dto) {
        return communityService.updateMenu(id, dto);
    }

    @DeleteMapping("menu/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        communityService.deleteMenu(id);
    }
}
