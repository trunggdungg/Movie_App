package com.example.movie_app.repository;

import com.example.movie_app.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    List<Country> findByName(String name);
    List<Country> findByNameContainingIgnoreCase(String name);

}
