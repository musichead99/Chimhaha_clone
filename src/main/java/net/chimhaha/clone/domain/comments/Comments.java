package net.chimhaha.clone.domain.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.BooleanToYNConverter;
import net.chimhaha.clone.domain.BaseTimeEntity;
import net.chimhaha.clone.domain.member.Member;
import net.chimhaha.clone.domain.posts.Posts;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Comments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Posts post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comments parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @BatchSize(size = 60)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
    private final List<Comments> children = new ArrayList<>();

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDeleted;

    @Builder
    public Comments(String content, Posts post, Comments parent, Member member) {
        this.content = content;
        this.post = post;
        this.parent = parent;
        this.member = member;
    }

    public void update(String content) {
        this.content = content;
    }

    public void changeDeleteStatus() {
        this.isDeleted = !this.isDeleted;
    }

    public boolean isParentExist() {
        return parent != null;
    }

    public boolean isChildrenExist() {
        return children.size() != 0;
    }

    public boolean isDeletable() {
        return children.size() == 1 && this.isDeleted;
    }

    public Comments getDeletableParents() {
        if(isParentExist()) {
            if(this.parent.isDeletable()) {
                return parent.getDeletableParents();
            }
        }

        return this;
    }

    @PrePersist
    public void prePersist() {
        isDeleted = isDeleted != null && isDeleted;
    }
}
