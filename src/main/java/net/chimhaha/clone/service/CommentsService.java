package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.comments.CommentsRepository;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.web.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.web.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.web.dto.comments.CommentsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentsService {

    private final PostsService postsService;
    private final CommentsRepository commentsRepository;

    @Transactional
    public Long save(CommentsSaveRequestDto dto) {

        /* 댓글을 작성할 게시글 조회 */
        Posts post = postsService.findPostsById(dto.getPostId());

        /* 대댓글이라면 부모 댓글 조회, 아니라면 그대로 null */
        Comments parent = null;
        if(dto.getParentId() != null) {
            parent = this.findById(dto.getPostId());

            if(!dto.getPostId().equals(parent.getPost().getId())) {
                throw new IllegalArgumentException("부모 댓글의 게시글 id와 자식 댓글의 게시글 id가 일치하지 않습니다.");
            }
        }

        /* 댓글 엔티티 생성 */
        Comments comment = Comments.builder()
                .content(dto.getContent())
                .parent(parent)
                .post(post)
                .build();

        return commentsRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public Page<CommentsFindByPostResponseDto> findByPost(Long postId, Pageable pageable) {

        /* 해당 게시글에 달린 댓글을 조회하기 위해 게시글 조회 */
        Posts post = postsService.findPostsById(postId);

        /* 게시글에 달린 모든 댓글을 페이징해 조회 */
        Page<Comments> comments = commentsRepository.findAllByPost(post, pageable);

        List<CommentsFindByPostResponseDto> dtoList = new ArrayList<>(); // 이후 PageImpl로 변환하기 위한 댓글 dto list
        Map<Long, CommentsFindByPostResponseDto> map = new HashMap<>(); // 댓글들을 계층 구조로 만들기 위한 맵

        /* 계층구조 변환 과정 */
        comments.stream()
                .map(CommentsFindByPostResponseDto::from)
                .forEach(dto -> {
                    map.put(dto.getId(), dto);

                    /* 현재 댓글이 대댓글이라면 */
                    if(dto.getParentId() != null) {
                        map.get(dto.getParentId()).getChildren().add(dto); // 부모 댓글에 children으로 추가
                    }
                    /* 대댓글이 아니라면 */
                    else {
                        dtoList.add(dto); // 그대로 list에 추가
                    }
                });

        /* 계층 구조로 변환이 완료된 댓글 리스트 PageImpl객체로 변환 */
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    @Transactional
    public Long update(Long id, CommentsUpdateRequestDto dto) {
        Comments comment = this.findById(id);

        comment.update(dto.getContent());

        return comment.getId();
    }

    @Transactional
    public void delete(Long id) {
        Comments comment = commentsRepository.findByIdWithParents(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다. id=" + id));

        if(comment.getChildren().size() != 0) {
            comment.changeDeleteStatus();
        } else {
            commentsRepository.delete(getDeletableParentComment(comment));
        }
    }

    private Comments getDeletableParentComment(Comments comment) {

        if(comment.getParent() != null) {
            Comments parent = commentsRepository.findByIdWithParents(comment.getParent().getId()).get();

            if(parent.getChildren().size() == 1 && parent.getIsDeleted()) {
                    return getDeletableParentComment(parent);
            }
        }

        return comment;
    }

    @Transactional(readOnly = true)
    public Comments findById(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다. id=" + id));
    }
}
