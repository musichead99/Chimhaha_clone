package net.chimhaha.clone.domain.images;

import net.chimhaha.clone.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

    List<Images> findByPost(Posts post);

    @Query("select i from Images i where i.post is null and i.id in :idList")
    List<Images> findByIdAndPostIsNullIn(List<Long> idList);

    @Query("select i from Images i where (i.post is null or i.post = :post) and i.id in :idList")
    List<Images> findByIdAndPostIsNullOrPostIn(List<Long> idList, Posts post);

    List<Images> findByPostIsNull();
}
