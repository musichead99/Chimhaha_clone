package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.comments.CommentsRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.domain.posts.PostsRepository;
import net.chimhaha.clone.web.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.web.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.web.dto.comments.CommentsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentsService {

    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;

    @Transactional
    public Long save(CommentsSaveRequestDto dto) {
        Posts post = postsRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comments parent = null;
        if(dto.getParentId() != null) {
            parent = commentsRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

            if(dto.getPostId() != parent.getPost().getId()) {
                throw new IllegalArgumentException("부모 댓글의 게시글 id와 자식 댓글의 게시글 id가 일치하지 않습니다.");
            }
        }

        Comments comment = Comments.builder()
                .content(dto.getContent())
                .parent(parent)
                .post(post)
                .build();

        return commentsRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public Page<CommentsFindByPostResponseDto> findByPost(Long postId, Pageable pageable) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Page<Comments> comments = commentsRepository.findAllByPost(post, pageable);

        List<CommentsFindByPostResponseDto> dtoList = new ArrayList<>();
        Map<Long, CommentsFindByPostResponseDto> map = new HashMap<>();

        comments.stream()
                .forEach(c -> {
                    CommentsFindByPostResponseDto dto = CommentsFindByPostResponseDto.from(c);
                    map.put(dto.getId(), dto);

                    if(dto.getParentId() != null) {
                        map.get(dto.getParentId()).getChildren().add(dto);
                    } else {
                        dtoList.add(dto);
                    }
                });

        return new PageImpl<CommentsFindByPostResponseDto>(dtoList, pageable, dtoList.size());
    }

    @Transactional
    public Long update(Long id, CommentsUpdateRequestDto dto) {
        Comments comment = commentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        comment.update(dto.getContent());

        return comment.getId();
    }

    @Transactional
    public void delete(Long id) {
        Comments comment = commentsRepository.findByIdWithParents(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if(comment.getChildren().size() != 0) {
            comment.changeDeleteStatus();
        } else {
            commentsRepository.delete(comment.getDeletableComment());
        }
    }
}
