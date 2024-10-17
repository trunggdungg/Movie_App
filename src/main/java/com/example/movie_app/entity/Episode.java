package com.example.movie_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    Integer displayOrder;
    Integer duration;
    String videoUrl;
    Boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime publishedAt;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    Movie movie;
}
