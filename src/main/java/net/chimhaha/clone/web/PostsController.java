package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.PostsService;
import net.chimhaha.clone.web.dto.PostsFindByCategoryResponseDto;
import net.chimhaha.clone.web.dto.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.PostsSaveRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;

    /* http status코드를 설정하기 위해 responseEntity 사용 */
    @PostMapping("/posts")
    public ResponseEntity<Long> save(@RequestBody PostsSaveRequestDto dto) {
        return new ResponseEntity<>(postsService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping("posts")
    public List<PostsFindByCategoryResponseDto> findByCategory(@RequestParam String category) {
        return postsService.findByCategory(category);
    }

    @GetMapping("posts/{id}")
    public PostsFindByIdResponseDto findById(@PathVariable("id")Long id) {
        return postsService.findById(id);
    }
}
