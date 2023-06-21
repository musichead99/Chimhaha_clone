package net.chimhaha.clone.controller.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.posts.Posts;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostsSaveResponseDto {
    private Long postId;
    private List<Long> imageIdList = new ArrayList<>();

    private PostsSaveResponseDto(Long postId) {
        this.postId = postId;
    }

    public static PostsSaveResponseDto from(Posts post) {
        return new PostsSaveResponseDto(post.getId());
    }

    public void setImageValues(List<Long> imageIds) {
        this.imageIdList = imageIds;
    }
}
