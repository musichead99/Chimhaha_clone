package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.BoardsService;
import net.chimhaha.clone.web.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("/boards")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long save(@RequestBody BoardsSaveRequestDto dto) {
        Long boardId = boardsService.save(dto);
        return boardId;
    }

    @GetMapping("/boards")
    public List<BoardsFindResponseDto> find() {
        return boardsService.find();
    }

    @PutMapping("/boards/{id}")
    public Long update(@PathVariable("id")Long id, @RequestBody BoardsUpdateRequestDto dto) {
        return boardsService.update(id, dto);
    }

    @DeleteMapping("/boards/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        boardsService.delete(id);
    }
}
