package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.boards.BoardsRepository;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.category.CategoryRepository;
import net.chimhaha.clone.web.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.web.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.web.dto.category.CategoryUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardsRepository boardsRepository;

    @Transactional
    public Long save(CategorySaveRequestDto dto) {

        Boards board = boardsRepository.getReferenceById(dto.getBoardId());

        Category category = categoryRepository.save(Category.builder()
                .name(dto.getName())
                .board(board)
                .build());

        return category.getId();
    }

    @Transactional(readOnly = true)
    public List<CategoryFindResponseDto> find() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryFindResponseDto> dtoList = new LinkedList<>();

        for(Category category : categories) {
            dtoList.add(CategoryFindResponseDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .boardId(category.getBoard().getId())
                    .boardName(category.getBoard().getName())
                    .build());
        }

        return dtoList;
    }

    @Transactional
    public Long update(Long id, CategoryUpdateRequestDto dto) {

        Boards board = boardsRepository.getReferenceById(dto.getBoardId());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 카테고리가 존재하지 않습니다."));

        category.update(dto.getName(), board);

        return category.getId();
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
