package net.chimhaha.clone.domain.images;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.BaseTimeEntity;
import net.chimhaha.clone.domain.member.Member;
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
    private int storedFileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Posts post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Images(String realFileName, String storedFileName, String storedFilePath, int storedFileSize, Member member, Posts post) {
        this.realFileName = realFileName;
        this.storedFileName = storedFileName;
        this.storedFilePath = storedFilePath;
        this.storedFileSize = storedFileSize;
        this.member = member;
        this.post = post;
    }

    public void attachedToPost(Posts post) {
        this.post = post;
    }

    /* 추후 애플리케이션의 전체적인 삭제 전략을 soft delete로 변경 시 추가적으로 상태를 변경하게끔 할 것 */
    public void delete() {
        this.post = null;
    }
}
