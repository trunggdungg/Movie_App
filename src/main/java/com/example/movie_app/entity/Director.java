package com.example.movie_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @Column(columnDefinition = "TEXT")
    String slug;
    String avatar;
    @Column(columnDefinition = "TEXT")
    String biography;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
