package com.example.movie_app.service;

import com.example.movie_app.entity.Episode;
import com.example.movie_app.entity.Movie;
import com.example.movie_app.model.request.CreateEpisodeRequest;
import com.example.movie_app.repository.EpisodeRepository;
import com.example.movie_app.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private  final MovieRepository movieRepository;
    public List<Episode> getEpisodesActiveByMovie(Integer movieId) {
        return episodeRepository.findByStatusAndMovie_IdOrderByDisplayOrderAsc(true, movieId);
    }

    public Episode getEpisodeByDisplayOrder(Integer movieId, String tap) {
        Integer covertTap = tap.equals("full") ? 1 : Integer.parseInt(tap);
        return episodeRepository
            .findByMovie_IdAndStatusAndDisplayOrder(movieId, true, covertTap)
            .orElse(null);
    }

    public List<Episode> getEpisodesByMovie(Integer movieId) {
        return episodeRepository.findByMovie_IdOrderByDisplayOrderAsc(movieId);
    }

    public Episode createEpisode(CreateEpisodeRequest request) {
        //Kiểm tra request.getMovieId() có tồn tại không
        Movie movie = movieRepository.findById(request.getMovieId())
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        //Kiem tra xem request.getDisplayOrder() đã tồn tại chưa
        Episode episode = (Episode) episodeRepository
            .findByMovie_IdAndDisplayOrder(request.getMovieId(), request.getDisplayOrder())
            .orElse(null);
        //Kiem tra loai phim co phai la phim le hay phim bo
        if (movie.getType().equals("PHIM_LE") || movie.getType().equals("PHIM_CHIEU_RAP")) {
            if (episode != null) {
                throw new RuntimeException("Display order is exist");
            }
        } else {
            if (episode != null) {
                throw new RuntimeException("Display order is exist");
            }
        }
        //Neu la phim le hay phim chieu rap thi displayOrder phai la 1, chi co 1 tap,neu da co roi thi bao loi
        if (movie.getType().equals("PHIM_LE") || movie.getType().equals("PHIM_CHIEU_RAP")) {
            if (request.getDisplayOrder() != 1) {
                throw new RuntimeException("Display order must be 1");
            }
        }
        //Neu la phim bo thi co nhieuf tap, displayOrder phai tu 1 tro len, khong duoc trung nhau
        if (movie.getType().equals("PHIM_BO")) {
            if (request.getDisplayOrder() < 1) {
                throw new RuntimeException("Display order must be greater than 1");
            }
        }

        // Create new Episode
        Episode newEpisode = new Episode();
        newEpisode.setTitle(request.getName());
        newEpisode.setDisplayOrder(request.getDisplayOrder());
        newEpisode.setStatus(request.getStatus());
        newEpisode.setMovie(movie);
        return episodeRepository.save(newEpisode);

    }

    public Episode updateEpisode(Integer id, CreateEpisodeRequest request) {
        return null;
    }

    public void deleteEpisode(Integer id) {
        Episode episode = episodeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Episode not found"));
        episodeRepository.delete(episode);
    }
}