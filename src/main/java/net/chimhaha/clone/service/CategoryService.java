package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.web.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.web.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.web.dto.category.CategoryUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardsService boardsService;

    @Transactional
    public Long save(CategorySaveRequestDto dto) {

        Boards board = boardsService.findById(dto.getBoardId());

        Category category = categoryRepository.save(Category.builder()
                .name(dto.getName())
                .board(board)
                .build());

        return category.getId();
    }

    @Transactional(readOnly = true)
    public List<CategoryFindResponseDto> find() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryFindResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, CategoryUpdateRequestDto dto) {

        Boards board = boardsService.findById(dto.getBoardId());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. id=" + id));

        category.update(dto.getName(), board);

        return category.getId();
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /* 서비스 계층 내에서만 사용할 메소드들 */
    Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. id=" + id));
    }
}
