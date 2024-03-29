package net.chimhaha.clone.domain.posts;

import net.chimhaha.clone.domain.boards.Boards;
import net.chimhaha.clone.domain.category.Category;
import net.chimhaha.clone.domain.menu.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    /* fetch join사용 시에는 반드시 countQuery도 커스텀해서 넣어 주어야 한다 */
    @Query(
            value = "select distinct p from Posts p join fetch p.board join fetch p.category join fetch p.menu join fetch p.menu",
            countQuery = "select count(p) from Posts p"
    )
    Page<Posts> findAll(Pageable pageable);

    @Query(
            value = "select distinct p from Posts p join fetch p.board join fetch p.category join fetch p.menu where p.category = :category",
            countQuery = "select count(p) from Posts p where p.category = :category"
    )
    Page<Posts> findByCategory(Category category, Pageable pageable);

    @Query(
            value = "select distinct p from Posts p join fetch p.board join fetch p.category join fetch p.menu where p.board = :board",
            countQuery = "select count(p) from Posts p where p.board = :board"
    )
    Page<Posts> findByBoard(Boards board, Pageable pageable);

    @Query(
            value = "select distinct p from Posts p join fetch p.board join fetch p.category join fetch p.menu where p.menu = :menu",
            countQuery = "select count(p) from Posts p where p.menu = :menu"
    )
    Page<Posts> findByMenu(Menu menu, Pageable pageable);
}
