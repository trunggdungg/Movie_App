package com.example.movie_app.repository;

import com.example.movie_app.entity.TokenConfirm;
import com.example.movie_app.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenConfirm,Integer> {

    Optional<TokenConfirm> findByToken(String token);

    Optional<TokenConfirm> findByTokenAndTokenType(String token, TokenType tokenType);
}
