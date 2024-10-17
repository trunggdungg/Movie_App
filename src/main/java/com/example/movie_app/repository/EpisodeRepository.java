package com.example.movie_app.repository;

import com.example.movie_app.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    List<Episode> findByStatusAndMovie_IdOrderByDisplayOrderAsc(boolean status, Integer movieId);

    Optional<Episode> findByMovie_IdAndStatusAndDisplayOrder(Integer movieId, boolean status, Integer displayOrder);

    Episode findByMovieIdAndStatusAndDisplayOrder(Integer id, boolean b, int i);

    List<Episode> findByMovie_IdOrderByDisplayOrderAsc(Integer movieId);

    Optional<Object> findByMovie_IdAndDisplayOrder(Integer movieId, Integer displayOrder);
}
