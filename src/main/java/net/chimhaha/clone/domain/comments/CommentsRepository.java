package net.chimhaha.clone.domain.comments;

import net.chimhaha.clone.domain.posts.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @Query(
            value = "select distinct c from Comments c where c.post = :post order by c.parent.id nulls first"
    )
    Page<Comments> findAllByPost(Posts post, Pageable pageable);

    @Query(
            value = "select distinct c from Comments c left join fetch c.parent left join fetch c.children where c.id = :id",
            countQuery = "select c from Comments c where c.id = :id"
    )
    Optional<Comments> findByIdWithParents(Long id);
}
