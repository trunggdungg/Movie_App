package com.example.movie_app.service;

import com.example.movie_app.entity.Movie;
import com.example.movie_app.entity.Review;
import com.example.movie_app.entity.User;
import com.example.movie_app.model.request.CreateReviewRequest;
import com.example.movie_app.model.request.UpdateReviewRequest;
import com.example.movie_app.repository.MovieRepository;
import com.example.movie_app.repository.ReviewRepository;
import com.example.movie_app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service// phai danh dau la @Service de spring boot biet day la 1 service
@RequiredArgsConstructor// hoặc dùng @Autowired
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    public List<Review> getReviewsByMovieId(Integer movieId) {
        return reviewRepository.findByMovie_Id(movieId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Review createReview(CreateReviewRequest request) {
       User user = (User) httpSession.getAttribute("CURRENT_USER");

        Movie movie = movieRepository.findById(request.getMovieId())
            .orElseThrow(() -> new RuntimeException("Movie not found with id: " + request.getMovieId()));

        // TODO: Bổ sung validate rating, content
        Review review = Review.builder()
            .content(request.getContent())
            .rating(request.getRating())
            .movie(movie)
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return reviewRepository.save(review);
    }

    public Review updateReview( UpdateReviewRequest request,Integer id) {
        User user = (User) httpSession.getAttribute("CURRENT_USER");

        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not owner of this review");
        }

        // TODO: Bổ sung validate rating, content
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public void deleteReview(Integer id) {
        User user = (User) httpSession.getAttribute("CURRENT_USER");

        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not owner of this review");
        }
        reviewRepository.delete(review);
    }
}
