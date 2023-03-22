package net.chimhaha.clone.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsFindResponseDto {
    private Long id;
    private String title;
    private String category;
    private String subject;
    private LocalDateTime createdDate;
    private Integer views;

    public PostsFindResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.category = posts.getCategory().getName();
        this.subject = posts.getSubject();
        this.createdDate = posts.getCreatedDate();
        this.views = posts.getViews();
    }
}
