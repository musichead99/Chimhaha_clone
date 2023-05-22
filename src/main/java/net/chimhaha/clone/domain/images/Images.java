package net.chimhaha.clone.domain.images;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.BaseTimeEntity;
import net.chimhaha.clone.domain.posts.Posts;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Images extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String realFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String storedFilePath;

    @Column(nullable = false)
    private Long storedFileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Posts post; // null이면 썸네일, null이 아니면 게시글 첨부 이미지

    @Builder
    public Images(String realFileName, String storedFileName, String storedFilePath, Long storedFileSize, Posts post) {
        this.realFileName = realFileName;
        this.storedFileName = storedFileName;
        this.storedFilePath = storedFilePath;
        this.storedFileSize = storedFileSize;
        this.post = post;
    }
}
