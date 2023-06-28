package net.chimhaha.clone.controller;

import lombok.RequiredArgsConstructor;
import net.chimhaha.clone.config.auth.CustomOAuth2User;
import net.chimhaha.clone.service.CommentsService;
import net.chimhaha.clone.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.dto.comments.CommentsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping(value = "/comments")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long save(@Valid @RequestBody CommentsSaveRequestDto dto,
                     @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return commentsService.save(dto, oAuth2User.getId());
    }

    @GetMapping(value = "/comments", params = "post")
    public Page<CommentsFindByPostResponseDto> findByPost(
            @RequestParam("post") Long postId,
            @PageableDefault(size = 60)Pageable pageable) {
        return commentsService.findByPost(postId, pageable);
    }

    @PutMapping("/comments/{id}")
    public Long update(@PathVariable("id") Long id, @Valid @RequestBody CommentsUpdateRequestDto dto) {
        return commentsService.update(id, dto);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        commentsService.delete(id);
    }
}
