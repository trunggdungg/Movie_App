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
public class SignupRequest {
    @NotEmpty(message = "Ten khong duoc de trong")
    String name;
    @NotEmpty(message = "Email khong duoc de trong")
        @Email(message = "Email khong dung dinh dang")
    String email;
    @NotEmpty(message = "Password khong duoc de trong")
        @Size(min = 6, message = "Password phai co it nhat 6 ky tu")
    String password;
}
