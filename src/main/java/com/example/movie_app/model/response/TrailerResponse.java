package com.example.movie_app.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrailerResponse {
    String url;
    Double duration;  // Thời lượng của video
}
