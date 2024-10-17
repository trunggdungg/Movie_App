package com.example.movie_app.repository;

import com.example.movie_app.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {




}
