package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.member.MemberRepository;
import net.chimhaha.clone.domain.member.MemberRole;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.domain.menu.MenuRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 테스트 메소드 이름에서 언더바 제거
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
public class PostsRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

    /* 단위 테스트 메소드 */
    @Test
    public void 게시글저장_불러오기() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .member(member)
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();
        Posts post = postsList.get(0);

        // then
        assertAll(() -> assertEquals("테스트 게시글", post.getTitle()),
                () -> assertEquals("테스트 본문", post.getContent()),
                () -> assertEquals("침착맨", post.getMenu().getName()),
                () -> assertEquals("침착맨", post.getBoard().getName()),
                () -> assertEquals("침착맨", post.getCategory().getName()),
                () -> assertEquals(0, post.getViews()),
                () -> assertEquals(true, post.getPopularFlag())
        );
    }

    @Test
    public void 페이징_게시글_전체_조회() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        for(int i = 0; i < 5; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .member(member)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page,size);

        // when
        Page<Posts> posts = postsRepository.findAll(pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize())
        );
    }

    @Test
    public void 게시글_수정() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Category updatedCategory = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨 짤")
                        .member(member)
                        .build()
        );

        Posts post = postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .member(member)
                .build());

        List<Images> images = new ArrayList<>();

        // when
        Posts savedPost = postsRepository.save(post);
        savedPost.update("테스트 게시글 2", "테스트 본문 2", updatedCategory, images, false);
        Posts updatedPost = postsRepository.save(savedPost);

        // then
        assertAll(
                () -> assertEquals(savedPost.getId(), updatedPost.getId()),
                () -> assertEquals("테스트 게시글 2", updatedPost.getTitle()),
                () -> assertEquals("테스트 본문 2", updatedPost.getContent()),
                () -> assertEquals("침착맨 짤", updatedPost.getCategory().getName())
        );
    }

    @Test
    public void 게시글_삭제() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Posts post = postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .menu(menu)
                .board(board)
                .category(category)
                .popularFlag(true)
                .member(member)
                .build());

        // when
        postsRepository.deleteById(post.getId());
        Optional<Posts> optionalPosts = postsRepository.findById(post.getId());

        // then
        assertFalse(optionalPosts.isPresent());
    }

    @Test
    public void 페이징_메뉴별_게시글_조회() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        int amount = 5;
        for(int i = 0; i < amount; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .member(member)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // when
        Page<Posts> posts = postsRepository.findByMenu(menu, pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize()),
                () -> assertEquals(amount, posts.getNumberOfElements()),
                () -> assertEquals(menu.getName(), posts.getContent().get(0).getMenu().getName())
        );

    }

    @Test
    public void 페이징_게시판별_게시글_조회() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        int amount = 5;
        for(int i = 0; i < amount; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .member(member)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // when
        Page<Posts> posts = postsRepository.findByBoard(board, pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize()),
                () -> assertEquals(amount, posts.getNumberOfElements()),
                () -> assertEquals(board.getName(), posts.getContent().get(0).getBoard().getName())
        );
    }

    @Test
    public void 페이징_카테고리별_게시글_조회() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이병건")
                .nickname("침착맨")
                .profileImage(" ")
                .memberRole(MemberRole.ADMIN)
                .email(" ")
                .provider("Naver")
                .build());

        Menu menu = menuRepository.save(
                Menu.builder()
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        Boards board = boardsRepository.save(
                Boards.builder()
                        .name("침착맨")
                        .description("침착맨에 대해 이야기하는 게시판입니다")
                        .menu(menu)
                        .likeLimit(20)
                        .member(member)
                        .build()
        );

        Category category = categoryRepository.save(
                Category.builder()
                        .board(board)
                        .name("침착맨")
                        .member(member)
                        .build()
        );

        int amount = 5;
        for(int i = 0; i < amount; i++) {
            postsRepository.save(Posts.builder()
                    .title("테스트 게시글")
                    .content("테스트 본문")
                    .menu(menu)
                    .board(board)
                    .category(category)
                    .popularFlag(true)
                    .member(member)
                    .build());
        }

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // when
        Page<Posts> posts = postsRepository.findByCategory(category, pageable);

        // then
        assertAll(
                () -> assertEquals(page, posts.getNumber()),
                () -> assertEquals(size, posts.getSize()),
                () -> assertEquals(5, posts.getNumberOfElements()),
                () -> assertEquals(board.getName(), posts.getContent().get(0).getBoard().getName())
        );
    }
}
