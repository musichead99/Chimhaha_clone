package net.chimhaha.clone.domain.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Boards board;

    @Builder
    public Category(String name, Boards board) {
        this.name = name;
        this.board = board;
    }

    public void update(String name, Boards board) {
        this.name = name;
        this.board = board;
    }
}
