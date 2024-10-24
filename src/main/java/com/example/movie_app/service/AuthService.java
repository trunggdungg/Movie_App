package com.example.movie_app.service;

import com.example.movie_app.entity.TokenConfirm;
import com.example.movie_app.entity.User;
import com.example.movie_app.exception.BadRequestException;
import com.example.movie_app.exception.NotFoundException;
import com.example.movie_app.model.TokenType;
import com.example.movie_app.model.User_Role;
import com.example.movie_app.model.request.*;
import com.example.movie_app.repository.TokenRepository;
import com.example.movie_app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpSession httpSession;
    private final TokenRepository tokenRepository;
    private final MailService mailService;

    public void login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }

        if (!user.getIs_active()) {
            throw new BadRequestException("User is not active");
        }
        // Lưu thông tin user vào session
        //hoac co the luu trong cookie,redis,db,...
        httpSession.setAttribute("CURRENT_USER", user);
    }

    public void logout() {
        httpSession.removeAttribute("CURRENT_USER");

    }

    public void signup(SignupRequest signupRequest) {
        Optional<User> userOptional = userRepository.findByEmail(signupRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new BadRequestException("Email is already taken");
        }
        User user = User.builder()
            .name(signupRequest.getName())
            .email(signupRequest.getEmail())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .is_active(false)
            .role(User_Role.USER)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        userRepository.save(user);

        //sinh token gui mail xac thuc
        TokenConfirm tokenConfirm = TokenConfirm.builder()
            .token(UUID.randomUUID().toString())
            .tokenType(TokenType.CONFIRM_REGISTER)
            .user(user)
            .createdAt(LocalDateTime.now())
            .expiredAt(LocalDateTime.now().plusHours(1))
            .build();
        tokenRepository.save(tokenConfirm);

        //gui mail xac thuc
        String link = "http://localhost:8082/xac-thuc-tai-khoan?token=" + tokenConfirm.getToken();
        System.out.println("Link xac thuc: " + link);

        mailService.sendMailResigter(user.getEmail(),
            "Xác thực tài khoản",
            "Click vào link sau để xác thực tài khoản: " + link);
    }

    public void updateName(UpdateProfileUserRequest updateProfileUserRequest) {
        User user = (User) httpSession.getAttribute("CURRENT_USER");
        user.setName(updateProfileUserRequest.getName());
        userRepository.save(user);
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = (User) httpSession.getAttribute("CURRENT_USER");
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public TokenConfirmMessageResponse verifyAccount(String token) {
        //kiem tra token co ton tai hay khong
        Optional<TokenConfirm> tokenConfirmOptional = tokenRepository.findByTokenAndTokenType(token, TokenType.CONFIRM_REGISTER);
        if (tokenConfirmOptional.isEmpty()) {
            return TokenConfirmMessageResponse.builder()
                .is_success(false)
                .message("Token khong ton tai")
                .build();

        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        // da duoc xac thuc hay chua
        if (tokenConfirm.getConfirmedAt() != null) {
            return TokenConfirmMessageResponse.builder()
                .is_success(false)
                .message("Token da duoc xac thuc")
                .build();
        }
        //kiem tra xem token da het han chua
        if (tokenConfirm.getExpiredAt().isBefore(LocalDateTime.now())) {
            return TokenConfirmMessageResponse.builder()
                .is_success(false)
                .message("Token da het han")
                .build();
        }

        //xac thuc tai khoan
        User user = tokenConfirm.getUser();
        user.setIs_active(true);
        userRepository.save(user);


        //cap nhat thoi gian xac thuc
        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(tokenConfirm);


        return TokenConfirmMessageResponse.builder()
            .is_success(true)
            .message("Xac thuc tai khoan thanh cong")
            .build();
    }

    public TokenConfirmMessageResponse verifyResetPasword(String token) {
        //kiem tra token co ton tai hay khong
        Optional<TokenConfirm> tokenConfirmOptional = tokenRepository.findByTokenAndTokenType(token, TokenType.FORGOT_PASSWORD);
        if (tokenConfirmOptional.isEmpty()) {
            return TokenConfirmMessageResponse.builder()
                .is_success(false)
                .message("Token khong ton tai")
                .build();

        }
        //token da duoc xac thuc hay chua
        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedAt() != null) {
            return TokenConfirmMessageResponse.builder()
                .is_success(false)
                .message("Token da duoc xac thuc")
                .build();
        }

        //kiem tra xem token da het han chua
        if (tokenConfirm.getExpiredAt().isBefore(LocalDateTime.now())) {
            return TokenConfirmMessageResponse.builder()
                .is_success(false)
                .message("Token da het han")
                .build();
        }


        //xac thuc thanh cong
        return TokenConfirmMessageResponse.builder()
            .is_success(true)
            .message("Xac thuc thanh cong")
            .build();

    }


    public void forgetPassword(ForrgetPaswordRequest email) {
        Optional<User> userOptional = userRepository.findByEmail(email.getEmail());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Email not found");
        }
        // neu co thi tao token va gui mail
        TokenConfirm tokenConfirm = TokenConfirm.builder()
            .token(UUID.randomUUID().toString())
            .tokenType(TokenType.FORGOT_PASSWORD)
            .user(userOptional.get())
            .createdAt(LocalDateTime.now())
            .expiredAt(LocalDateTime.now().plusHours(1))
            .build();
        tokenRepository.save(tokenConfirm);

        //STring link
        String link = "http://localhost:8082/dat-lai-mat-khau?token=" + tokenConfirm.getToken();

        System.out.println("Link xac thuc: " + link);

        mailService.sendMailResigter(userOptional.get().getEmail(),
            "Xác thực tài khoản",
            "Click vào link sau để xác thực tài khoản: " + link);
    }


    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        //kiem tra token co ton tai hay khong
        Optional<TokenConfirm> tokenConfirmOptional = tokenRepository.findByTokenAndTokenType(resetPasswordRequest.getToken(), TokenType.FORGOT_PASSWORD);
        if (tokenConfirmOptional.isEmpty()) {
            throw new NotFoundException("Token not found");
        }
        //token da duoc xac thuc hay chua
        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedAt() != null) {
            throw new BadRequestException("Token da duoc xac thuc");
        }

        //kiem tra xem token da het han chua
        if (tokenConfirm.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token da het han");
        }

        //xac thuc thanh cong
        User user = tokenConfirm.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);

        //cap nhat thoi gian xac thuc
        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(tokenConfirm);



    }
}
