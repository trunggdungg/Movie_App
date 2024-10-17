package com.example.movie_app;

import com.example.movie_app.entity.*;
import com.example.movie_app.model.Movie_Type;
import com.example.movie_app.model.User_Role;
import com.example.movie_app.model.request.CreateEpisodeRequest;
import com.example.movie_app.model.request.UpsertMovieRequest;
import com.example.movie_app.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
class MovieAppApplicationTests {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DirectorRepository  directorRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private GenresRepository genreRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    //là bean cần inject vào nơi cần sử dụng


@Test
void getEpisodesByMovie() {
    List<Episode> episodes  = episodeRepository.findByMovie_IdOrderByDisplayOrderAsc(6);
    System.out.println("Episodes: " + episodes);
}

    @Test
    void findCountry() {
//        Optional<Country> country = countryRepository.findById(1);
//        System.out.println("ct" + country);

        List<Genres> genres = genreRepository.findAllById(List.of(1, 2, 3));
        System.out.println("Genres: " + genres);
    }
    //
    @Test
    void findMovieById() {
        Movie movie = movieRepository.findMovieById(2);
        System.out.println("Movie: " + movie);
    }

    @Test
    void encode_user_password() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        }
    }

    @Test
    void save_users() {
        Faker faker = new Faker();
        for (int i = 0; i < 50; i++) {
            String name = faker.name().fullName();
            User user = User.builder()
                .name(name)
                .email(faker.internet().emailAddress())
                .avatar("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
                .password("123")
                .role(i == 0 || i == 1 ? User_Role.ADMIN.ADMIN : User_Role.USER.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            userRepository.save(user);
        }
    }


    @Test
    void save_genres() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 10; i++) {
            String name = faker.leagueOfLegends().champion();
            Genres genre = Genres.builder()
                .name(name)
                .slug(slugify.slugify(name))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            genreRepository.save(genre);
        }
    }

    @Test
    void save_countries() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 10; i++) {
            String name = faker.country().name();
            Country country = Country.builder()
                .name(name)
                .slug(slugify.slugify(name))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            countryRepository.save(country);
        }
    }

    @Test
    void save_actors() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 100; i++) {
            String name = faker.name().fullName();
            Actor actor = Actor.builder()
                .name(name)
                .slug(slugify.slugify(name))
                .avatar("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
                .biography(faker.lorem().paragraph())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            actorRepository.save(actor);
        }
    }


    @Test
    void save_directors() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 20; i++) {
            String name = faker.name().fullName();
            Director director = Director.builder()
                .name(name)
                .slug(slugify.slugify(name))
                .avatar("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
                .biography(faker.lorem().paragraph())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            directorRepository.save(director);
        }
    }



    @Test
    void save_blogs() {
        Faker faker = new Faker();
        Random rd = new Random();
        Slugify slugify = Slugify.builder().build();

        List<User> users = userRepository.findByRole(User_Role.ADMIN);

        for (int i = 0; i < 100; i++) {
            // random 1 user
            User rdUser = users.get(rd.nextInt(users.size()));

            String title = faker.book().title();
            boolean status = rd.nextInt(2) == 0;
            Blog blog = Blog.builder()
                .title(title)
                .slug(slugify.slugify(title))
                .description(faker.lorem().paragraph())
                .content(faker.lorem().paragraph(100))
                .status(status)
                .thumbnail(generateLinkImage(title))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .publishedAt(status ? LocalDateTime.now() : null)
                .user(rdUser)
                .build();

            blogRepository.save(blog);
        }
    }


    @Test
    void save_movies() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random rd = new Random();

        List<Country> countries = countryRepository.findAll();
        List<Actor> actors = actorRepository.findAll();
        List<Director> directors = directorRepository.findAll();
        List<Genres> genres = genreRepository.findAll();

        for (int i = 0; i < 150; i++) {
            // Random 1 country
            Country rdCountry = countries.get(rd.nextInt(countries.size()));

            // Random 1 -> 3 genres
            List<Genres> rdGenres = new ArrayList<>();
            for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                Genres rdGenre = genres.get(rd.nextInt(genres.size()));
                if (!rdGenres.contains(rdGenre)) {
                    rdGenres.add(rdGenre);
                }
            }

            // Random 5 -> 7 actors
            List<Actor> rdActors = new ArrayList<>();
            for (int j = 0; j < rd.nextInt(3) + 5; j++) {
                Actor rdActor = actors.get(rd.nextInt(actors.size()));
                if (!rdActors.contains(rdActor)) {
                    rdActors.add(rdActor);
                }
            }

            // Random 1 -> 3 directors
            List<Director> rdDirectors = new ArrayList<>();
            for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                Director rdDirector = directors.get(rd.nextInt(directors.size()));
                if (!rdDirectors.contains(rdDirector)) {
                    rdDirectors.add(rdDirector);
                }
            }

            String name = faker.name().fullName();
            Boolean status = faker.bool().bool();
            Movie movie = Movie.builder()
                .name(name)
                .slug(slugify.slugify(name))
                .description(faker.lorem().paragraph())
                .poster("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
                .releaseYear(faker.number().numberBetween(2020, 2024))
                .rating(faker.number().randomDouble(1, 6, 10))
                .trailerUrl("https://www.youtube.com/embed/gCUg6Td5fgQ?si=OCtNkpFF03gq03ny")
                .type(Movie_Type.values()[rd.nextInt(Movie_Type.values().length)])
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .publishedAt(status ? LocalDateTime.now() : null)
                .country(rdCountry)
                .actors(rdActors)
                .directors(rdDirectors)
                .genres(rdGenres)
                .build();
            movieRepository.save(movie);
        }
    }

    @Test
    void save_reviews() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<Movie> movies = movieRepository.findAll();
        List<User> users = userRepository.findAll();

        for (Movie movie : movies) {
            // Create 10 -> 20 reviews for each movie
            for (int i = 0; i < rd.nextInt(11) + 10; i++) {
                // Random 1 user
                User rdUser = users.get(rd.nextInt(users.size()));

                Review review = Review.builder()
                    .content(faker.lorem().paragraph())
                    .rating(rd.nextInt(6) + 5)
                    .movie(movie)
                    .user(rdUser)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                reviewRepository.save(review);
            }
        }
    }@Test
    void save_episodes() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            if (movie.getType().equals(Movie_Type.PHIM_BO)) {
                // Random 5 -> 10 episodes for each movie
                for (int i = 0; i < rd.nextInt(6) + 5; i++) {
                    Episode episode = Episode.builder()
                        .title("Tap " + (i + 1))
                        .duration(rd.nextInt(31) + 30)
                        .displayOrder(i + 1)
                        .videoUrl("https://www.youtube.com/embed/gCUg6Td5fgQ?si=OCtNkpFF03gq03ny")
                        .movie(movie)
                        .status(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .publishedAt(LocalDateTime.now())
                        .build();
                    episodeRepository.save(episode);
                }
            } else {
                Episode episode = Episode.builder()
                    .title("Tap full")
                    .duration(rd.nextInt(91) + 30)
                    .displayOrder(1)
                    .videoUrl("https://www.youtube.com/embed/gCUg6Td5fgQ?si=OCtNkpFF03gq03ny")
                    .movie(movie)
                    .status(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .publishedAt(LocalDateTime.now())
                    .build();
                episodeRepository.save(episode);
            }
        }
    }














    private String generateLinkImage(String name) {
        return "https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase();
    }
    @Test
    void test_method () {
        List<Movie> movies = movieRepository.findAll();
        System.out.println("Size: " + movies.size());

        Movie movie1 = movieRepository.findById(1).orElse(null);
        System.out.println("Movie 1: " + movie1);


        List<Movie> movie2 = movieRepository.findAllById(List.of(1, 2, 3));
        System.out.println("Movie 2: " + movie2);


        //update
        movie1.setName("Phim 1");
        movieRepository.save(movie1);

        //delete
        movieRepository.deleteById(2);

    }

    @Test
    public void testPhim(){
        long countPhim = movieRepository.countByType(Movie_Type.PHIM_BO);
        System.out.println("Phim bo: " + countPhim);
    }

    @Test
    public void testgetphim(){
        List<Movie> movie = movieRepository.findTop5ByTypeAndStatusOrderByCreatedAtDescRatingAsc(Movie_Type.PHIM_BO, false);
        System.out.println("Phim bo: " + movie);
    }

    @Test
    public void sortPhim() {
        List<Movie> movie = movieRepository.findByStatus(false, Sort.by("name", "releaseYear").descending());
        for (   Movie movie1 : movie
             ) {
            System.out.println(movie1);
        }
    }

    @Test
    public void pagePhim() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movie> pageData = movieRepository.findByStatus(false,pageable);
        System.out.println("total page: " + pageData.getTotalPages());
        System.out.println("total element: " + pageData.getTotalElements());
        System.out.println("Is first: " + pageData.isFirst());

       pageData.getContent().forEach(System.out::println);

    }

    @Test
    public void testGet(){
      Movie movie3  =   movieRepository.findByNameAndSlug("Phim 1", "leesa-schuster-iii");
        System.out.println("Movie 3: " + movie3);

        Movie movie4 = movieRepository.findMovieByNameAndSlug("Phim 1", "leesa-schuster-iii");
        System.out.println("Movie 4: " + movie4);
    }

    @Test
    public void testPhimHot(){
        List<Movie> movie = movieRepository.findTop4ByType(Movie_Type.PHIM_HOT, Sort.by("rating").descending());
        System.out.println("Phim hot: " + movie);
    }

    @Test
    public void getMoviesByType(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Movie> movie = movieRepository.findByTypeAndStatus(Movie_Type.PHIM_BO, true, pageable);
        System.out.println("Phim bo: " + movie.getContent());
    }

    @Test
    @Transactional
    void testFindByMovieIdAndStatus() {
        Movie movie = movieRepository.findById(1).orElse(null);
        // Access the genres collection to initialize it
        movie.getGenres().size();
        // Test findByMovieIdAndStatus
        Episode foundEpisode = episodeRepository.findByMovieIdAndStatusAndDisplayOrder(movie.getId(), true, 1);
        System.out.println("Episode: " + foundEpisode);
    }
}
