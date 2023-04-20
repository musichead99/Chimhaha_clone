package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 게시글을 작성할 때 전달받는 dto */
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private Long menuId;
    private Long boardId;
    private Long categoryId;
    private Boolean popularFlag;

    @Builder
    public PostsSaveRequestDto(String title, String content, Long menuId, Long boardId, Long categoryId, Boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.menuId = menuId;
        this.boardId = boardId;
        this.categoryId = categoryId;
        this.popularFlag = popularFlag;
    }

}
