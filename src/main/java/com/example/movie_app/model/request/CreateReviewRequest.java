package com.example.movie_app.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString

public class CreateReviewRequest {
    String content;
    Integer rating;
    Integer movieId;

}
