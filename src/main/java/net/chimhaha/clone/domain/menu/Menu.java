package net.chimhaha.clone.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.BaseTimeEntity;
import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.member.Member;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "menu")
    private final List<Boards> boards = new LinkedList<>();

    @Builder
    public Menu(String name, Member member) {
        this.name = name;
        this.member = member;
    }

    public void update(String name) {
        this.name = name;
    }
}