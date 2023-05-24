package net.chimhaha.clone.web;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.service.ImagesService;
import net.chimhaha.clone.service.PostsService;
import net.chimhaha.clone.utils.FileUploadService;
import net.chimhaha.clone.web.dto.posts.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;
    private final FileUploadService fileUploadService;
    private final ImagesService imagesService;

    /* 이미지를 첨부하지 않은 게시글 업로드 */
    @PostMapping(value = "/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public PostsSaveResponseDto save(@RequestBody PostsSaveRequestDto dto) {
        return postsService.save(dto);
    }

    /* 이미지를 첨부한 게시글 업로드 */
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<PostsSaveResponseDto> save(@RequestPart(value = "postsSaveRequestDto") PostsSaveRequestDto dto, @RequestPart(value = "images") List<MultipartFile> images) {

        List<File> uploadedImages = new ArrayList<>(fileUploadService.upload(images)); // 실제 파일 저장

        Long postId = postsService.save(dto, images); // 게시글 DB에 등록

        List<Long> uploadedImagesId =  imagesService.save(postId, uploadedImages, images); // DB에 파일 정보 등록

        PostsSaveResponseDto responseDto = PostsSaveResponseDto.builder()
                .postId(postId)
                .requestCount(images.size())
                .uploadedCount(uploadedImages.size())
                .imageIds(uploadedImagesId)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /* 쿼리스트링으로 page=1&size=20&sort=id&direction=DESC 형식의 파라미터 필요
    *  여기서는 PageableDefault로 기본 설정을 정해두었기 때문에 page만 파라미터로 입력하면 자동으로 페이징이 이루어진다. */
    @GetMapping("/posts")
    public Page<PostsFindResponseDto> find(@PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.find(pageable);
    }

    @GetMapping(path = "/posts", params = "category")
    public Page<PostsFindResponseDto> findByCategory(@RequestParam("category")Long categoryId, @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByCategory(categoryId, pageable);
    }

    @GetMapping(path = "/posts", params = "board")
    public Page<PostsFindResponseDto> findByBoard(@RequestParam("board")Long boardId, @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByBoard(boardId, pageable);
    }

    @GetMapping(path = "/posts", params = "menu")
    public Page<PostsFindResponseDto> findByMenu(@RequestParam("menu")Long menuId, @PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return postsService.findByMenu(menuId, pageable);
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
