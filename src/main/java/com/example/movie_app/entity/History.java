package com.example.movie_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "histories")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
   double duration;
   LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    Movie movie;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    Episode episode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
