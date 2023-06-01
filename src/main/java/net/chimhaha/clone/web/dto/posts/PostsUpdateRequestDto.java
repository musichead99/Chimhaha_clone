package net.chimhaha.clone.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    String title;
    String content;
    Long categoryId;
    List<Long> imageIdList;
    Boolean popularFlag;

    @Builder
    public PostsUpdateRequestDto(String title, String content, Long categoryId, List<Long> imageIdList, Boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.imageIdList = imageIdList;
        this.popularFlag = popularFlag;
    }
}
