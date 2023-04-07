package net.chimhaha.clone.domain.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.menu.Menu;
import net.chimhaha.clone.web.dto.boards.BoardsUpdateRequestDto;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "like_limit", nullable = false)
    private Integer likeLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Menu menu;

    @Builder
    public Boards(String name, String description, Menu menu, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.menu = menu;
        this.likeLimit = likeLimit;
    }

    public void update(BoardsUpdateRequestDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.likeLimit = dto.getLikeLimit();
    }
}
