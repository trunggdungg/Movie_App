package com.example.movie_app.repository;

import com.example.movie_app.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {

    List<Actor> findByName(String name);
    List<Actor> findByNameContainingIgnoreCase(String name);
}
