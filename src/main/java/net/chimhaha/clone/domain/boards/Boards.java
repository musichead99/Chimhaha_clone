package net.chimhaha.clone.domain.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Builder
    public Boards(String name, String description, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }

    public void update(BoardsUpdateRequestDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.likeLimit = dto.getLikeLimit();
    }
}
