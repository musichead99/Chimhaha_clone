package net.chimhaha.clone.domain.category;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRepository;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.dto.category.CategoryUpdateRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardsRepository boardsRepository;

    String name = "침착맨";

    @Test
    public void 카테고리_등록() {
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
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .menu(menu)
                .likeLimit(10)
                .member(member)
                .build();

        boardsRepository.save(board);

        Category category = Category.builder()
                .name(name)
                .board(board)
                .member(member)
                .build();

        // when
        Category storedCategory = categoryRepository.save(category);

        // then
        assertAll(
                () -> assertNotNull(storedCategory.getId()), // 매번 deleteAll()을 해도 auto_increament값은 초기화되지 않으므로 id가 존재하는지만 테스트
                () -> assertEquals(category.getName(), storedCategory.getName()),
                () -> assertEquals(category.getBoard().getName(), storedCategory.getBoard().getName())
        );
    }

    @Test
    public void 카테고리_전체_조회() {
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
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .menu(menu)
                .member(member)
                .likeLimit(10)
                .build();

        boardsRepository.save(board);

        int amount = 5;
        for(int i = 0; i < amount; i++) {
            categoryRepository.save(Category.builder()
                    .name(name)
                    .board(board)
                    .member(member)
                    .build());
        }

        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(amount, categories.size()),
                () -> assertEquals(name, categories.get(0).getName()),
                () -> assertEquals(board.getName(), categories.get(0).getBoard().getName())
        );
    }

    @Test
    public void 카테고리_수정() {
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
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .menu(menu)
                .member(member)
                .likeLimit(10)
                .build();

        boardsRepository.save(board);

        Category category = Category.builder()
                .name(name)
                .board(board)
                .member(member)
                .build();

        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .name("침착맨 짤")
                .boardId(board.getId())
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);
        category.update(dto.getName(), board);
        Category updatedCategory = categoryRepository.save(category);

        // then
        assertAll(
                () -> assertEquals(savedCategory.getId(), updatedCategory.getId()),
                () -> assertEquals(dto.getName(), updatedCategory.getName()),
                () -> assertEquals(dto.getBoardId(), updatedCategory.getBoard().getId())
        );

    }

    @Test
    public void 카테고리_삭제() {
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
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .menu(menu)
                .member(member)
                .likeLimit(10)
                .build();

        boardsRepository.save(board);

        Category category = categoryRepository.save(Category.builder()
                .name(name)
                .board(board)
                .member(member)
                .build());
        Long categoryId = category.getId();

        // when
        categoryRepository.deleteById(categoryId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        // then
        assertFalse(optionalCategory.isPresent());
    }
}