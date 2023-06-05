package net.chimhaha.clone.web.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.posts.Posts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor // dto to json 변환시 objectmapper가 이 생성자를 사용한다
public class PostsFindByIdResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private List<Long> imageIdList;
    private LocalDateTime createdDate;
    private Integer views;

    public static PostsFindByIdResponseDto from(Posts post) {
        return new PostsFindByIdResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getImages().stream().map(Images::getId).collect(Collectors.toList()),
                post.getCreatedDate(),
                post.getViews());
    }
}
