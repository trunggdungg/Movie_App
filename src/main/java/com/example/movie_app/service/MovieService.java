package com.example.movie_app.service;

import com.example.movie_app.entity.*;
import com.example.movie_app.model.Movie_Type;
import com.example.movie_app.model.request.UpsertMovieRequest;
import com.example.movie_app.repository.*;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final CountryRepository countryRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenresRepository genresRepository;
    private final CloudinaryService cloudinaryService;

    public Page<Movie> getMoviesByType(Movie_Type type, Boolean status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return movieRepository.findByTypeAndStatus(type, status, pageable);
    }//page  -1 vi page bat dau tu 0 con trang bat dau tu 1 nên phải trừ đi 1 để lấy đúng trang

    public List<Movie> getMovieHot() {
        return movieRepository.findTop4ByType(Movie_Type.PHIM_LE, Sort.by("rating").descending());
    }

    public Movie getMovieDetails(Integer id, String slug) {
        return movieRepository.findByIdAndSlugAndStatus(id, slug, true)
            .orElse(null);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by("createdAt").descending());
    }

    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id)
            .orElse(null);
    }

    public Movie updateMovie(Integer id, UpsertMovieRequest request) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found"));

        Country country = countryRepository.findById(request.getCountryId())
            .orElseThrow(() -> new RuntimeException("Country not found"));

        List<Genres> genres = genresRepository.findAllById(request.getGenreIds());
        List<Actor> actors = actorRepository.findAllById(request.getActorIds());
        List<Director> directors = directorRepository.findAllById(request.getDirectorIds());

        Slugify slugify = Slugify.builder().build();
        movie.setName(request.getName());
        movie.setSlug(slugify.slugify(request.getName()));
        movie.setDescription(request.getDescription());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setStatus(request.getStatus());
        movie.setType(request.getType());

        movie.setCountry(country);
        movie.setGenres(genres);
        movie.setActors(actors);
        movie.setDirectors(directors);
        movie.setUpdatedAt(LocalDateTime.now());
        movie.setPublishedAt(request.getStatus() ? LocalDateTime.now() : null);
        movieRepository.save(movie);

        return movie;
    }

    public Movie createMovie(UpsertMovieRequest request) {
        Slugify slugify = Slugify.builder().build();
        LocalDateTime now = LocalDateTime.now();

        // Lấy country từ request
        Country country = countryRepository.findById(request.getCountryId())
            .orElseThrow(() -> new EntityNotFoundException ("Country not found with id: " + request.getCountryId()));

        // Lấy genres từ request
        List<Genres> genres = genresRepository.findAllById(request.getGenreIds());

        // Lấy actors từ request
        List<Actor> actors = actorRepository.findAllById(request.getActorIds());

        // Lấy directors từ request
        List<Director> directors = directorRepository.findAllById(request.getDirectorIds());
        String name = request.getName();
        Movie movie = Movie.builder()
            .name(name)
            .slug(slugify.slugify(name))
            .description(request.getDescription())
            .releaseYear(request.getReleaseYear())
            .poster("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
            .trailerUrl(request.getTrailerUrl())
            .status(request.getStatus())
            .type(request.getType())
            .country(country)
            .genres(genres)
            .actors(actors)
            .directors(directors)
            .createdAt(now)
            .updatedAt(now)
            .publishedAt(request.getStatus() ? now : null)
            .build();

        return movieRepository.save(movie);
    }


    public String uploadPoster(Integer id, MultipartFile file) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        try {
            Map map = cloudinaryService.uploadFile(file, "java-25-movie");
            System.out.println("map = " + map);
            String path = map.get("url").toString();
            movie.setPoster(path);
            movieRepository.save(movie);
            return map.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file");
        }

    }


    public Map<String, Object> uploadTrailer(Integer id, MultipartFile file) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        try {
            Map<String, Object> map = cloudinaryService.uploadVideo(file, "java-25-movie");
            System.out.println("map = " + map);
            String path = map.get("url").toString();
            movie.setTrailerUrl(path);
            movieRepository.save(movie);
            return map;  // Trả về toàn bộ map, chứa cả url và duration
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }



    public Movie saveMoviePoster(Integer movieId, String posterPath) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setPoster(posterPath);
        return movieRepository.save(movie);
    }

    public void deleteEpisode(Integer id) {
        movieRepository.deleteById(id);
    }
}
