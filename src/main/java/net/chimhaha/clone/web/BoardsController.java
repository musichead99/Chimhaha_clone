package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.BoardsService;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("/boards")
    public ResponseEntity<Long> save(@RequestBody BoardsSaveRequestDto dto) {
        Long boardId = boardsService.save(dto);
        return new ResponseEntity<>(boardId, HttpStatus.CREATED);
    }
}
