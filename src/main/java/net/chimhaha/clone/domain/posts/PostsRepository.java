package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    /* fetch join사용 시에는 반드시 countQuery도 커스텀해서 넣어 주어야 한다 */
    @Query(
            value = "select distinct p from Posts p join fetch p.board",
            countQuery = "select count(p) from Posts p"
    )
    Page<Posts> findAll(Pageable pageable);

    List<Posts> findBySubject(String Subject);
    List<Posts> findByBoard(Boards board);
}
