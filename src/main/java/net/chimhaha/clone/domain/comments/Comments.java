package net.chimhaha.clone.domain.comments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.converter.BooleanToYNConverter;
import net.chimhaha.clone.domain.BaseTimeEntity;
import net.chimhaha.clone.domain.posts.Posts;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
    private List<Comments> children = new ArrayList<>();

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDeleted;

    @Builder
    public Comments(String content, Posts post, Comments parent) {
        this.content = content;
        this.post = post;
        this.parent = parent;
    }

    public void update(String content) {
        this.content = content;
    }

    public void changeDeleteStatus() {
        this.isDeleted = !this.isDeleted;
    }

    @PrePersist
    public void prePersist() {
        isDeleted = isDeleted == null ? false: isDeleted;
    }
}
