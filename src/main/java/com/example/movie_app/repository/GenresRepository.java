package com.example.movie_app.repository;

import com.example.movie_app.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenresRepository extends JpaRepository<Genres, Integer> {
List<Genres> findByName(String name);

List<Genres> findByNameContainingIgnoreCase(String name);
Genres findBySlug(String slug);

}
