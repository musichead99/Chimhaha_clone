package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.CategoryService;
import net.chimhaha.clone.web.dto.category.CategoryFindResponseDto;
import net.chimhaha.clone.web.dto.category.CategorySaveRequestDto;
import net.chimhaha.clone.web.dto.category.CategoryUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Long> save(@RequestBody CategorySaveRequestDto dto) {
        return new ResponseEntity<Long>(categoryService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public List<CategoryFindResponseDto> find() {
        return categoryService.find();
    }

    @PutMapping("/category/{id}")
    public Long update(@PathVariable("id") Long id, @RequestBody CategoryUpdateRequestDto dto) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
