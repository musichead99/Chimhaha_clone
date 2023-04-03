package net.chimhaha.clone.web.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor // dto to json 변환시 objectmapper가 이 생성자를 사용한다
public class PostsFindByIdResponseDto {
    private Long id;
    private String title;
    private String content;
    private String subject;
    private LocalDateTime createdDate;
    private Integer views;

    public static PostsFindByIdResponseDto from(Posts post) {
        return new PostsFindByIdResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSubject(),
                post.getCreatedDate(),
                post.getViews());
    }
}
