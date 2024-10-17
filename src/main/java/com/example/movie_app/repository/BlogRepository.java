package com.example.movie_app.repository;

import com.example.movie_app.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Blog findBySlug(String slug);
    List<Blog> findByTitleContainingIgnoreCase(String title);
    List<Blog> findByStatusOrderByPublishedAtDesc(Boolean status);
    Page<Blog> findByStatus(Boolean status, Pageable pageable);

    Optional<Blog> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

}
