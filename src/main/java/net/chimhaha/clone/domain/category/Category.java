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

    private String description;

    @Column(name = "like_limit", nullable = false)
    private Integer likeLimit;

    @Builder
    public Category(String name, String description, Integer likeLimit) {
        this.name = name;
        this.description = description;
        this.likeLimit = likeLimit;
    }
}
