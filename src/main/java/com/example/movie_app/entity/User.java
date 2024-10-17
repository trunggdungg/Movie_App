package com.example.movie_app.entity;

import com.example.movie_app.model.User_Role;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @Enumerated(EnumType.STRING)
    User_Role role;
    String email;
    String password;
    String avatar;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // tra ve giao dien thi dung @ConTroller
    // tra ve du lieu json thi dung @RestController

}
