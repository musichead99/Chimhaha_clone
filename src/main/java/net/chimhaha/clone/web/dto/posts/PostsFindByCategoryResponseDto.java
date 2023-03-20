package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsFindByCategoryResponseDto {
    private Long id;
    private String title;
    private String category;
    private LocalDateTime createdDate;
    private Integer views;

    public PostsFindByCategoryResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.category = posts.getCategory();
        this.createdDate = posts.getCreatedDate();
        this.views = posts.getViews();
    }
}
