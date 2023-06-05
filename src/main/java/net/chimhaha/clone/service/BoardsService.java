package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.web.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final MenuService menuService;

    @Transactional
    public Long save(BoardsSaveRequestDto dto) {
        Menu menu = menuService.findById(dto.getMenuId());

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
        Boards board = this.findById(id);

        board.update(dto.getName(), dto.getDescription(), dto.getLikeLimit());
        return board.getId();
    }

    @Transactional
    public void delete(Long id) {
        boardsRepository.deleteById(id);
    }


    /* 서비스 계층 내에서만 사용할 메소드들 */

    @Transactional(readOnly = true)
    Boards findById(Long id) {
        return boardsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판을 찾을 수 없습니다. id=" + id));
    }

}
