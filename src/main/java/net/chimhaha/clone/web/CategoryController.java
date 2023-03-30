package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.CategoryService;
import net.chimhaha.clone.web.dto.category.CategorySaveRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Long> save(@RequestBody CategorySaveRequestDto dto) {
        return new ResponseEntity<Long>(categoryService.save(dto), HttpStatus.CREATED);
    }
}
