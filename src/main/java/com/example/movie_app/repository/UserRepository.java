package com.example.movie_app.repository;

import com.example.movie_app.entity.User;
import com.example.movie_app.model.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    long countUserByRole(User_Role role);

    List<User> findByRole(User_Role userRole);

    // dung Optional de tranh truong hop null,tuc la user cos the co hoac khong
    Optional<User> findByEmail(String email);
}
