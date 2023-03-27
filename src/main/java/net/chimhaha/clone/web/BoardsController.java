package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.BoardsService;
import net.chimhaha.clone.web.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("/boards")
    public ResponseEntity<Long> save(@RequestBody BoardsSaveRequestDto dto) {
        Long boardId = boardsService.save(dto);
        return new ResponseEntity<>(boardId, HttpStatus.CREATED);
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
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boardsService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
