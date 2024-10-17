package com.example.movie_app.repository;

import com.example.movie_app.entity.Movie;
import com.example.movie_app.model.Movie_Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByName(String name);// findBy... theo cái gì đấy phải đúng theo tên thuộc tính

    List<Movie> findByNameContainingIgnoreCase(String name);
    Boolean existsMovieByName(String name);
    Movie findMovieById(Integer id);
    Movie findByNameAndSlug(String name, String slug);

    List<Movie> findByRatingBetween(Double min, Double max);

    List<Movie> findByReleaseYearGreaterThanEqual(Integer year);

    List<Movie> findByStatusOrderByReleaseYearDesc(Boolean status);

    Page<Movie> findByStatus(Boolean status, Pageable pageable);
    List<Movie> findByStatus(Boolean status, Sort sort);

    long countByType(Movie_Type type);

    boolean existsByRatingGreaterThan(Double rating);


    // tim kiem phim theo loia va staus sau do sap xep theo thoi gian tao giam dan , rating tang dan va lay 5 ban ghi dau tien


    List<Movie> findTop5ByTypeAndStatusOrderByCreatedAtDescRatingAsc(Movie_Type type, Boolean status);



    //ứng dụng movie

    Page<Movie> findByTypeAndStatus(Movie_Type type,Boolean status, Pageable pageable);// phân trang voi 1 loại phim và status

    Movie findMovieByNameAndSlug(String name, String slug);

    List<Movie> findTop4ByType(Movie_Type type, Sort rating);

    //phim dề xuat
    // nhung phim cung loai khong chua phim dang xem
    // lay theo rateting giam dan
    // lay 6 ban ghi
    //status = true
    List<Movie> findTop6ByTypeAndStatusAndNameNot(Movie_Type type, Boolean status, String name, Sort rating);
    Optional<Movie> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);
}
