package net.chimhaha.clone.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(
            value = "select distinct c from Category c join fetch c.board",
            countQuery = "select count(c) from Category c")
    List<Category> findAll();
}
