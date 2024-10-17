package com.example.movie_app.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class UpdateEpisodeRequest {
    @NotEmpty(message = "Tên tập phim không được để trống")
    String name;
    @NotNull(message = "Thu tu phim không được để trống")
    @Min(value = 1, message = "Thu tu phim phải lớn hơn hoặc bằng 1")
    Integer displayOrder;

   @NotEmpty(message = "Trạng thái không được để trống")
    Boolean status;

}
