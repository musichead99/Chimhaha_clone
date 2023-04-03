package net.chimhaha.clone.web.dto.posts;

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
    private String board;
    private String subject;
    private LocalDateTime createdDate;
    private Integer views;

    public static PostsFindResponseDto from(Posts post) {
        return new PostsFindResponseDto(
                post.getId(),
                post.getTitle(),
                post.getBoard().getName(),
                post.getSubject(),
                post.getCreatedDate(),
                post.getViews()
        );
    }
}
