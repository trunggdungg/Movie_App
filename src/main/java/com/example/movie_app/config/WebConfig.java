package com.example.movie_app.config;

import com.example.movie_app.config.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//la mot annotation giup Spring Boot biet rang day la mot class cau hinh
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    //Khai bao doi tuong AuthenticationInterceptor,vi la bean nen Spring Boot se tu dong inject vao
    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns(
                "/api/reviews",
                "/api/reviews/**",
                "/api/blogs/**",
                "/thong-tin-ca-nhan",
                "/phim-yeu-thich",
                "/lich-su-xem-phim"
                //khong co api la tra va giao dien,co api la tra ve json
            );
    }
}
