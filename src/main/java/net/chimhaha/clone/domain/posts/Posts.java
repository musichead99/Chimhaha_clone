package net.chimhaha.clone.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.converter.BooleanToYNConverter;
import net.chimhaha.clone.domain.BaseTimeEntity;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.comments.Comments;
import net.chimhaha.clone.domain.images.Images;
import net.chimhaha.clone.domain.menu.Menu;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/* setter 메소드가 없다 : 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확히 구분할 수 없기 때문
* 해당 필드의 값 변경이 필요하다면 반드시 그 목적과 의도를 알 수 있는 메소드를 추가해야 함
*  */
@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    @Column()
    @ColumnDefault("0")
    private Integer views; // 조회수

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "popular_flag", nullable = false)
    private Boolean popularFlag; // 인기글(침하하) 허용 플래그

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Menu menu;

    /* fk설정 해제 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Boards board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comments> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Images> images = new ArrayList<>();

    @Builder
    public Posts(String title, String content, Menu menu, Boards board, Category category, Integer views, Boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.menu = menu;
        this.board = board;
        this.category = category;
        this.views = views;
        this.popularFlag = popularFlag;
    }

    public void update(String title, String content, Category category, List<Images> images, boolean popularFlag) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.images = images;
        this.popularFlag = popularFlag;
    }

    public void increaseViewCount() {
        views++;
    }

    public void addAttachedImages(List<Images> images) {
        this.images.addAll(images);
    }

    /* @prePersist 어노테이션은 새로 생성된 엔티티가 영속상태가 되기 이전에 실행된다.
    * 이 경우는 디폴트값이 0이어야 하는 조회수에 insert시 null값이 들어가는 문제 때문에 사용했다.  */
    @PrePersist
    public void prePersist() {
        this.views = this.views == null ? 0 : this.views;
    }
}
