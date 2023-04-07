package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import net.chimhaha.clone.web.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public Long save(BoardsSaveRequestDto dto) {
        Menu menu = menuRepository.getReferenceById(dto.getMenuId());

        Boards board = Boards.builder()
                .menu(menu)
                .name(dto.getName())
                .description(dto.getDescription())
                .likeLimit(dto.getLikeLimit())
                .build();

        return boardsRepository.save(board).getId();
    }

    @Transactional(readOnly = true)
    public List<BoardsFindResponseDto> find() {
        List<Boards> boards = boardsRepository.findAll();

        return boards.stream()
                .map(BoardsFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, BoardsUpdateRequestDto dto) {
        Boards board = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시판이 존재하지 않습니다."));

        board.update(dto);
        return board.getId();
    }

    @Transactional
    public void delete(Long id) {
        boardsRepository.deleteById(id);
    }

}
