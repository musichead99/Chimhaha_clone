package net.chimhaha.clone.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsFindByIdResponseDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdDate;
    private Integer views;

    @Builder
    public PostsFindByIdResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.category = posts.getCategory();
        this.createdDate = posts.getCreatedDate();
        this.views = posts.getViews();
    }
}
