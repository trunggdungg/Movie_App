package com.example.movie_app.controller;

import com.example.movie_app.entity.*;
import com.example.movie_app.model.Movie_Type;
import com.example.movie_app.repository.*;
import com.example.movie_app.service.EpisodeService;
import com.example.movie_app.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/movies")
// /("api/") là trả v json
public class MovieController {
    private final MovieService movieService;
    private final CountryRepository countryRepository;
    private  final GenresRepository genresRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private  final EpisodeService episodeService;


    @GetMapping
    public String getIndexPage(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/movie/index";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("genres", genresRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());
        return "admin/movie/create";
    }

    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("genres", genresRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());


        // lay danh sách tập phim của phim sắp xếp theo thứ tự tăng dần
       List<Episode> episodes = episodeService.getEpisodesByMovie(id);
        model.addAttribute("episodes", episodes);
        return "admin/movie/detail";
    }

}
