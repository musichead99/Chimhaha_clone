package net.chimhaha.clone.domain.images;

import net.chimhaha.clone.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

    List<Images> findByPost(Posts post);

    List<Images> findByIdIn(List<Long> id);
}
