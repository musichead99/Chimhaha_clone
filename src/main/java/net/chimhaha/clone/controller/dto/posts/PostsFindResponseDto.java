package net.chimhaha.clone.controller.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostsFindResponseDto {
    private Long id;
    private String title;
    private Long menuId;
    private String menuName;
    private Long boardId;
    private String boardName;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdDate;
    private Integer views;

    public static PostsFindResponseDto from(Posts post) {
        return new PostsFindResponseDto(
                post.getId(),
                post.getTitle(),
                post.getMenu().getId(),
                post.getMenu().getName(),
                post.getBoard().getId(),
                post.getBoard().getName(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getCreatedDate(),
                post.getViews()
        );
    }
}
