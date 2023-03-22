package net.chimhaha.clone.domain.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String content;

    @Column(name = "like_limit", nullable = false)
    private Integer likeLimit;

    @Builder
    public Category(String name, String content, Integer likeLimit) {
        this.name = name;
        this.content = content;
        this.likeLimit = likeLimit;
    }
}
