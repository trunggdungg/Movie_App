package com.example.movie_app.rest;

import com.example.movie_app.entity.Review;
import com.example.movie_app.model.request.CreateReviewRequest;
import com.example.movie_app.model.request.UpdateReviewRequest;
import com.example.movie_app.service.ReviewService;
import com.example.movie_app.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // dùng reqiredArgsConstructor phải có final
@RequestMapping("/api/reviews")

public class ReivewAPI {

    private final ReviewService reviewService;//ben service khai bao la serrvice thi moi dung dc hoac la dug @Autowired
    @PostMapping
    //tao review,tham so truyen vao la 1 reviewRequest, sau do tra ve 1 responseEntity
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest createReviewRequest){
       try {
              Review review = reviewService.createReview(createReviewRequest);
              return ResponseEntity.ok(review);
       }catch (Exception e){
           ErrorResponse errorResponse = ErrorResponse.builder()
               .status(HttpStatus.BAD_REQUEST)
               .message(e.getMessage())
               .build();
           return ResponseEntity.badRequest().body(errorResponse);
       }
    }

    ///api/reviews/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest updateReviewRequest, @PathVariable Integer id){
        try {
            Review review = reviewService.updateReview(updateReviewRequest,id);
            return ResponseEntity.ok(review);
        }catch (Exception e){
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id){
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
