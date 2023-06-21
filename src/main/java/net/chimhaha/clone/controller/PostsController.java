package net.chimhaha.clone.controller;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.PostsService;
import net.chimhaha.clone.controller.dto.posts.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;

    /* 게시글 업로드 */
    @PostMapping(value = "/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public PostsSaveResponseDto save(@Valid @RequestBody PostsSaveRequestDto dto) {
        return postsService.save(dto);
    }

    /* 쿼리스트링으로 page=1&size=20&sort=id&direction=DESC 형식의 파라미터 필요
    *  여기서는 PageableDefault로 기본 설정을 정해두었기 때문에 page만 파라미터로 입력하면 자동으로 페이징이 이루어진다. */
    @GetMapping("/posts")
    public Page<PostsFindResponseDto> find(
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.find(pageable);
    }

    @GetMapping(path = "/posts", params = "category")
    public Page<PostsFindResponseDto> findByCategory(
            @RequestParam("category")Long categoryId,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByCategory(categoryId, pageable);
    }

    @GetMapping(path = "/posts", params = "board")
    public Page<PostsFindResponseDto> findByBoard(
            @RequestParam("board")Long boardId,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByBoard(boardId, pageable);
    }

    @GetMapping(path = "/posts", params = "menu")
    public Page<PostsFindResponseDto> findByMenu(
            @RequestParam("menu")Long menuId,
            @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByMenu(menuId, pageable);
    }

    @GetMapping("/posts/{id}")
    public PostsFindByIdResponseDto findById(@PathVariable("id")Long id) {
        return postsService.findById(id);
    }

    @PutMapping("/posts/{id}")
    public Long update(@PathVariable("id")Long id, @Valid @RequestBody PostsUpdateRequestDto dto) {
        return postsService.update(id, dto);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        postsService.delete(id);
    }
}
