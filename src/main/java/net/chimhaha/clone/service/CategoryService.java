package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.dto.category.CategoryUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long save(CategorySaveRequestDto dto, Member member, Boards board) {

        Category category = categoryRepository.save(Category.builder()
                .name(dto.getName())
                .board(board)
                .member(member)
                .build());

        return category.getId();
    }

    @Transactional(readOnly = true)
    public List<Category> find() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Long update(Boards board, Category category, CategoryUpdateRequestDto dto) {

        category.update(dto.getName(), board);

        return category.getId();
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /* 서비스 계층 내에서만 사용할 메소드들 */
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
