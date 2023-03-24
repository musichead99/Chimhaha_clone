package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    List<Posts> findBySubject(String Subject);
    List<Posts> findByBoard(Boards board);
}
