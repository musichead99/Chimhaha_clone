package net.chimhaha.clone.dto.comments;

import lombok.Getter;
import net.chimhaha.clone.domain.comments.Comments;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentsFindByPostResponseDto {
    private final Long id;
    private final Long parentId;
    private final String content;
    private final LocalDateTime createdDate;
    private final Boolean isDeleted;
    private final List<CommentsFindByPostResponseDto> children = new ArrayList<>();

    private CommentsFindByPostResponseDto(Long id, Long parentId, String content, LocalDateTime createdDate, Boolean isDeleted) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
        this.createdDate = createdDate;
        this.isDeleted = isDeleted;
    }

    public static CommentsFindByPostResponseDto from(Comments comment) {
        Long parentId = null;
        if(comment.getParent() != null) {
            parentId = comment.getParent().getId();
        }

        return new CommentsFindByPostResponseDto(
                comment.getId(),
                parentId,
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getIsDeleted()
        );
    }
}
