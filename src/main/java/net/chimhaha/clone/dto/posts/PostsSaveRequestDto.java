package net.chimhaha.clone.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/* 게시글을 작성할 때 전달받는 dto */
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Long menuId;

    @NotNull
    private Long boardId;

    @NotNull
    private Long categoryId;

    private List<Long> imageIdList;

    @NotNull
    private Boolean popularFlag;

    @Builder
    public PostsSaveRequestDto(String title, String content, Long menuId, Long boardId, Long categoryId, List<Long> imageIdList, Boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.menuId = menuId;
        this.boardId = boardId;
        this.categoryId = categoryId;
        this.imageIdList = imageIdList;
        this.popularFlag = popularFlag;
    }

}
