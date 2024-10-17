package com.example.movie_app.config.interceptor;

import com.example.movie_app.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("CURRENT_USER");
        // Kiem tra xem user da dang nhap chua
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            return false;
        }
        return true;
    }
}
