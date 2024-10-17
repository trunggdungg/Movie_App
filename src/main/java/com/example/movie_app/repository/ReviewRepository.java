package com.example.movie_app.repository;

import com.example.movie_app.entity.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByContent(String content);
    List<Review> findByRatingBetween(int min, int max);

    List<Review> findByMovie_Id(Integer movieId, Sort sort);
}
