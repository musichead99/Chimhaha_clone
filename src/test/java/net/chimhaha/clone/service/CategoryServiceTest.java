package net.chimhaha.clone.service;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.web.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.web.dto.category.CategorySaveRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BoardsRepository boardsRepository;

    @InjectMocks
    private CategoryService categoryService;

    String name = "침착맨";

    @Test
    public void 카테고리_등록() {
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

        CategorySaveRequestDto dto = CategorySaveRequestDto.builder()
                .name(name)
                .boardId(1L)
                .build();

        given(categoryRepository.save(any(Category.class)))
                .willReturn(category);
        given(boardsRepository.getReferenceById(any(Long.class)))
                .willReturn(board);

        // when
        Long createdCategoryId = categoryService.save(dto);

        // then
        assertAll(
                () -> assertEquals(categoryId, createdCategoryId),
                () -> verify(categoryRepository, times(1)).save(any(Category.class)),
                () -> verify(boardsRepository, times(1)).getReferenceById(any(Long.class))
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
        List<CategoryFindResponseDto> dtoList = categoryService.find();

        // then
        assertAll(
                () -> assertEquals(amount, dtoList.size()),
                () -> assertEquals(categories.get(0).getId(), dtoList.get(0).getId()),
                () -> assertEquals(categories.get(0).getName(), dtoList.get(0).getName()),
                () -> assertEquals(categories.get(0).getBoard().getId(), dtoList.get(0).getBoardId()),
                () -> assertEquals(categories.get(0).getBoard().getName(), dtoList.get(0).getBoardName()),
                () -> verify(categoryRepository, times(1)).findAll()
        );

    }
}
