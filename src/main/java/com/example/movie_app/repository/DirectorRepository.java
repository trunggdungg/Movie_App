package com.example.movie_app.repository;

import com.example.movie_app.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectorRepository extends JpaRepository<Director, Integer> {
List<Director> findByName(String name);
List<Director> findByNameContainingIgnoreCase(String name);
}
