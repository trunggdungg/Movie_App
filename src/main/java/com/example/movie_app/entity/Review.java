package com.example.movie_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition = "TEXT")
    String content;
    Integer rating;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    Movie movie;
    // nhieu review thuoc ve 1 movie
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    // nhieu review thuoc ve 1 user
}
