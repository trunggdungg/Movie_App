package com.example.movie_app.entity;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;

    String slug;
    @Column(columnDefinition = "MEDIUMTEXT")
    String content;
    @Column(columnDefinition = "TEXT")
    String description;
    String thumbnail;
    Boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime publishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    // nhieu blog thuoc ve 1 user


}
