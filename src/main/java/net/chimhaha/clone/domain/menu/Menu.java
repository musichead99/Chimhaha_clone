package net.chimhaha.clone.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.chimhaha.clone.domain.boards.Boards;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "menu")
    private final List<Boards> boards = new LinkedList<>();

    @Builder
    public Menu(String name) {
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }
}
