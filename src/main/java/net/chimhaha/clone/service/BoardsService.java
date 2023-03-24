package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.web.dto.boards.BoardsSaveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
