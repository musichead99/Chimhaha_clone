package net.chimhaha.clone.domain.boards;

import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRepository;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.dto.boards.BoardsUpdateRequestDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class BoardsRepositoryTest {

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private MemberRepository memberRepository;

    static String name = "침착맨";
    static String description = "침착맨에 대해 이야기하는 게시판입니다";
    static Integer likeLimit = 10;

    @Test
    public void 게시판_전체_조회() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu,"id", 1L);

        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .menu(menu)
                .likeLimit(likeLimit)
                .member(member)
                .build();

        int ea = 1;
        boardsRepository.save(board);

        // when
        List<Boards> boards = boardsRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(ea, boards.size()),
                () -> assertEquals(name, boards.get(0).getName()),
                () -> assertEquals(description, boards.get(0).getDescription()),
                () -> assertEquals(likeLimit, boards.get(0).getLikeLimit())
        );

    }

    @Test
    public void 게시판_등록() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu,"id", 1L);

        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .menu(menu)
                .likeLimit(likeLimit)
                .member(member)
                .build();
        Long expectedId = 1L;

        // when
        Boards createdBoard = boardsRepository.save(board);

        // then
        assertAll(() -> assertEquals(expectedId, createdBoard.getId()),
                () -> assertEquals(board.getName(), createdBoard.getName()),
                () -> assertEquals(board.getDescription(), createdBoard.getDescription()),
                () -> assertEquals(board.getLikeLimit(), createdBoard.getLikeLimit())
        );
    }

    @Test
    public void 게시판_수정() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = Menu.builder()
                .name("침착맨")
                .build();
        ReflectionTestUtils.setField(menu,"id", 1L);

        Boards board = Boards.builder()
                .name(name)
                .description(description)
                .menu(menu)
                .likeLimit(10)
                .member(member)
                .build();

        BoardsUpdateRequestDto dto = BoardsUpdateRequestDto.builder()
                .name("대인국(주펄)")
                .description("주호민 자치령")
                .likeLimit(20)
                .build();

        // when
        Boards savedBoard = boardsRepository.save(board);
        savedBoard.update(dto.getName(), dto.getDescription(), dto.getLikeLimit());
        Boards updatedBoard = boardsRepository.save(savedBoard);

        // then
        assertAll(() -> assertEquals(savedBoard.getId(), updatedBoard.getId()),
                () -> assertEquals("대인국(주펄)", updatedBoard.getName()),
                () -> assertEquals("주호민 자치령", updatedBoard.getDescription()),
                () -> assertEquals(20, updatedBoard.getLikeLimit()));
    }

    @Disabled
    @Test
    public void 게시판_삭제() {

    }
}
