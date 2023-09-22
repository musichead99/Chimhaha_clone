package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.dto.category.CategoryUpdateRequestDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    String name = "침착맨";

    @Test
    public void 카테고리_등록() {
        // given
        CategorySaveRequestDto dto = CategorySaveRequestDto.builder()
                .name(name)
                .boardId(1L)
                .build();

        Boards board = mock(Boards.class);
        Category category = mock(Category.class);
        Member member = mock(Member.class);

        given(categoryRepository.save(any(Category.class)))
                .willReturn(category);
        given(category.getId())
                .willReturn(1L);

        // when
        Long createdCategoryId = categoryService.save(dto, member, board);

        // then
        assertAll(
                () -> assertEquals(1L, createdCategoryId),
                () -> verify(categoryRepository, times(1)).save(any(Category.class))
        );
    }

    @Test
    public void 카테고리_전체_조회() {
        // given
        Boards board = Boards.builder()
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();

        List<Category> categories = new ArrayList<>();
        int amount = 5;

        for(int i = 0; i < amount; i++) {
            categories.add(Category.builder()
                    .name(name)
                    .board(board)
                    .build());
        }

        given(categoryRepository.findAll())
                .willReturn(categories);

        // when
        List<Category> categoryList = categoryService.find();

        // then
        assertAll(
                () -> assertEquals(amount, categoryList.size()),
                () -> assertEquals(categories.get(0).getId(), categoryList.get(0).getId()),
                () -> assertEquals(categories.get(0).getName(), categoryList.get(0).getName()),
                () -> assertEquals(categories.get(0).getBoard().getId(), categoryList.get(0).getBoard().getId()),
                () -> assertEquals(categories.get(0).getBoard().getName(), categoryList.get(0).getBoard().getName()),
                () -> verify(categoryRepository, times(1)).findAll()
        );
    }

    @Test
    public void 카테고리_수정() {
        // given
        Boards board = Boards.builder()
                .name("침착맨")
                .description("침착맨에 대해 이야기하는 게시판입니다")
                .likeLimit(10)
                .build();
        Long boardId = 1L;
        ReflectionTestUtils.setField(board, "id", boardId);

        Category category = Category.builder()
                .name(name)
                .board(board)
                .build();
        Long categoryId = 1L;
        ReflectionTestUtils.setField(category, "id", categoryId);

        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .name(name)
                .boardId(boardId)
                .build();

        // when
        Long updatedCategoryId = categoryService.update(board, category, dto);

        // then
        assertAll(
                () -> assertEquals(categoryId, updatedCategoryId)
        );

    }

    @Test
    public void 카테고리_삭제() {
        // given
        Long categoryId = 1L;

        willDoNothing().given(categoryRepository).deleteById(any(Long.class));

        // when
        categoryService.delete(categoryId);

        // then
        verify(categoryRepository, times(1)).deleteById(any(Long.class));

    }
}
