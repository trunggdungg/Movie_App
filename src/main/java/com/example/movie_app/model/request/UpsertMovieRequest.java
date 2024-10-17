package com.example.movie_app.model.request;

import com.example.movie_app.model.Movie_Type;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertMovieRequest {
    String name;
    String description;
    Integer releaseYear;
    String trailerUrl;
    Boolean status;
    Movie_Type type;
    Integer countryId;
    List<Integer> genreIds;
    List<Integer> actorIds;
    List<Integer> directorIds;
}
