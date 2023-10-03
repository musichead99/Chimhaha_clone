package net.chimhaha.clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.comments.CommentsRepository;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.posts.Posts;
import net.chimhaha.clone.exception.CustomException;
import net.chimhaha.clone.exception.ErrorCode;
import net.chimhaha.clone.dto.comments.CommentsFindByPostResponseDto;
import net.chimhaha.clone.dto.comments.CommentsSaveRequestDto;
import net.chimhaha.clone.dto.comments.CommentsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    @Transactional
    public Long save(CommentsSaveRequestDto dto, Member member, Posts post) {

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
                .member(member)
                .build();

        return commentsRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public Page<Comments> findByPost(Pageable pageable, Posts post) {

        /* 게시글에 달린 모든 댓글을 페이징해 조회 */
        return commentsRepository.findAllByPost(post, pageable);
    }

    @Transactional
    public Long update(CommentsUpdateRequestDto dto, Comments comment) {

        comment.update(dto.getContent());

        return comment.getId();
    }

    @Transactional
    public void delete(Comments comment) {

        if(comment.isChildrenExist()) {
            comment.changeDeleteStatus();
            return;
        }

        commentsRepository.delete(comment.getDeletableParents());
    }

    @Transactional(readOnly = true)
    public Comments findById(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENTS_NOT_FOUND));
    }

    /* 계층 구조로 변환 */
    List<CommentsFindByPostResponseDto> toHierarchy(Page<Comments> comments) {

        List<CommentsFindByPostResponseDto> list = new ArrayList<>(); // 이후 PageImpl로 변환하기 위한 댓글 dto list
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
                        list.add(dto); // 그대로 list에 추가
                    }
                });

        return list;
    }
}
