package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.dto.boards.BoardsSaveRequestDto;
import net.chimhaha.clone.dto.boards.BoardsUpdateRequestDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardsService {

    private final BoardsRepository boardsRepository;

    @Transactional
    public Long save(BoardsSaveRequestDto dto, Menu menu, Member member) {

        Boards board = Boards.builder()
                .menu(menu)
                .name(dto.getName())
                .description(dto.getDescription())
                .likeLimit(dto.getLikeLimit())
                .member(member)
                .build();

        return boardsRepository.save(board).getId();
    }

    @Transactional(readOnly = true)
    public List<Boards> find() {
        return boardsRepository.findAll();
    }

    @Transactional
    public Long update(Boards board, BoardsUpdateRequestDto dto) {
        board.update(dto.getName(), dto.getDescription(), dto.getLikeLimit());

        return board.getId();
    }

    @Transactional
    public void delete(Long id) {
        boardsRepository.deleteById(id);
    }


    /* 서비스 계층 내에서만 사용할 메소드들 */

    @Transactional(readOnly = true)
    public Boards findById(Long id) {
        return boardsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARDS_NOT_FOUND));
    }

}
