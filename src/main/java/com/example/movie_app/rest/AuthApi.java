package com.example.movie_app.rest;

import com.example.movie_app.entity.Review;
import com.example.movie_app.model.request.LoginRequest;
import com.example.movie_app.model.request.SignupRequest;
import com.example.movie_app.model.request.UpdatePasswordRequest;
import com.example.movie_app.model.request.UpdateProfileUserRequest;
import com.example.movie_app.model.response.ErrorResponse;
import com.example.movie_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {//@Valid để kiểm tra dữ liệu đầu vào à mới định nghĩa bên phần request
        authService.login(loginRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            authService.signup(signupRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/update-name")
    public ResponseEntity<?> updateName(@RequestBody UpdateProfileUserRequest updateProfileUserRequest) {
        try {
            authService.updateName(updateProfileUserRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        try {
            authService.updatePassword(updatePasswordRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
