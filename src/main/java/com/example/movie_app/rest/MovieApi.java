package com.example.movie_app.rest;


import com.example.movie_app.entity.Movie;
import com.example.movie_app.entity.Review;
import com.example.movie_app.model.request.UpsertMovieRequest;
import com.example.movie_app.model.response.ErrorResponse;
import com.example.movie_app.model.response.FileResponse;
import com.example.movie_app.model.response.TrailerResponse;
import com.example.movie_app.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/movies")// /("api/") là trả v json
public class MovieApi {
    private final MovieService movieService;




    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Integer id,
                                         @RequestBody UpsertMovieRequest request) {
        Movie movie = movieService.updateMovie(id, request);
        return ResponseEntity.ok(movie);
    }

    //delete



    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody UpsertMovieRequest request) {
        try {
            Movie movie = movieService.createMovie(request);
            return ResponseEntity.ok(movie);
        }catch (Exception e){
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @PostMapping("/{id}/upload-poster")
    public ResponseEntity<?> uploadPoster(@PathVariable Integer id, @RequestParam MultipartFile file) {//MultipartFile file để upload file,la dai dien cac file dc tu client gui len
        String path = movieService.uploadPoster(id, file);
        FileResponse fileResponse = FileResponse.builder()
            .url(path)
            .build();
        return ResponseEntity.ok(fileResponse);
    }

    @PostMapping("/{id}/upload-trailer")
    public ResponseEntity<?> uploadTrailer(@PathVariable Integer id, @RequestParam MultipartFile file) {

        Map<String, Object> uploadResult = movieService.uploadTrailer(id, file);

        // Lấy URL và duration từ kết quả upload
        String path = uploadResult.get("url").toString();
        Double duration = Double.parseDouble(uploadResult.get("duration").toString());

        // Tạo TrailerResponse với url và duration
        TrailerResponse trailerResponse = TrailerResponse.builder()
            .url(path)
            .duration(duration)
            .build();

        return ResponseEntity.ok(trailerResponse);
    }


}
