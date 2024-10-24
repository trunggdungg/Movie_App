package com.example.movie_app.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class LoginRequest {
    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    String password;
}
