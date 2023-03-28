package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.PostsService;
import net.chimhaha.clone.web.dto.posts.PostsFindResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsFindByIdResponseDto;
import net.chimhaha.clone.web.dto.posts.PostsSaveRequestDto;
import net.chimhaha.clone.web.dto.posts.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

    /* 쿼리스트링으로 page=1&size=20&sort=id&direction=DESC 형식의 파라미터 필요
    *  여기서는 PageableDefault로 기본 설정을 정해두었기 때문에 page만 파라미터로 입력하면 자동으로 페이징이 이루어진다. */
    @GetMapping("/posts")
    public Page<PostsFindResponseDto> find(@PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.find(pageable);
    }

    @GetMapping(path = "/posts", params = "subject")
    public List<PostsFindResponseDto> findBySubject(@RequestParam("subject")String subject) {
        return postsService.findBySubject(subject);
    }

    @GetMapping(path = "/posts", params = "board")
    public Page<PostsFindResponseDto> findByBoard(@RequestParam("board")Long boardId, @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByBoard(boardId, pageable);
    }

    @GetMapping("/posts/{id}")
    public PostsFindByIdResponseDto findById(@PathVariable("id")Long id) {
        postsService.increaseViewCount(id);
        return postsService.findById(id);
    }

    @PutMapping("/posts/{id}")
    public Long update(@PathVariable("id")Long id, @RequestBody PostsUpdateRequestDto dto) {
        return postsService.update(id, dto);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        postsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
