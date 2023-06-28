package net.chimhaha.clone.controller;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.config.auth.CustomOAuth2User;
import net.chimhaha.clone.service.BoardsService;
import net.chimhaha.clone.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.dto.boards.BoardsUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("/boards")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long save(@Valid @RequestBody BoardsSaveRequestDto dto,
                     @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return boardsService.save(dto, oAuth2User.getId());
    }

    @GetMapping("/boards")
    public List<BoardsFindResponseDto> find() {
        return boardsService.find();
    }

    @PutMapping("/boards/{id}")
    public Long update(@PathVariable("id")Long id, @Valid @RequestBody BoardsUpdateRequestDto dto) {
        return boardsService.update(id, dto);
    }

    @DeleteMapping("/boards/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        boardsService.delete(id);
    }
}
