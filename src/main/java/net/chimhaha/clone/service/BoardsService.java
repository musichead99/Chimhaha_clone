package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.web.dto.boards.BoardsFindResponseDto;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsFindResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardsService {

    private final BoardsRepository boardsRepository;

    @Transactional
    public Long save(BoardsSaveRequestDto dto) {
        Boards board = Boards.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .likeLimit(dto.getLikeLimit())
                .build();

        return boardsRepository.save(board).getId();
    }

    @Transactional(readOnly = true)
    public List<BoardsFindResponseDto> find() {
        List<Boards> boards = boardsRepository.findAll();

        return makeEntityToDto(boards);
    }

    @Transactional
    public Long update(Long id, BoardsUpdateRequestDto dto) {
        Boards board = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시판이 존재하지 않습니다."));

        board.update(dto);
        return board.getId();
    }

    private List<BoardsFindResponseDto> makeEntityToDto(List<Boards> boards) {
        List<BoardsFindResponseDto> responses = new ArrayList<>();

        for(Boards board : boards) {
            responses.add(new BoardsFindResponseDto(board));
        }
        return responses;
    }
}
