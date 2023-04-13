package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.dao.FilmDao;
import ru.yandex.practicum.filmorate.dal.dao.LikeListDao;
import ru.yandex.practicum.filmorate.dal.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class FilmDaoTest {
    private final FilmDao filmStorage;
    private final UserDao userDao;
    private final MpaRatingDao mpaRatingDao;
    private final LikeListDao likeListDao;


    @Order(1)
    @Test
    void getAllFilms_shouldReturnCorrectList_afterPutFilms() throws SQLException {
        filmStorage.add(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());
        filmStorage.add(Film.builder()
                .name("test1")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());
        filmStorage.add(Film.builder()
                .name("test2")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());

        List<Film> films = (List<Film>) filmStorage.getValues();

        assertThat(films.get(0).getName()).isEqualTo("test");
        assertThat(films.get(1).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getId()).isEqualTo(3);
        assertThat(films.size()).isEqualTo(3);
        assertThat(films.get(2).getName()).isEqualTo("test2");
        assertThat(films.get(2).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getReleaseDate()).isNotNull();
        assertThat(films.get(2).getDuration()).isEqualTo(100);
        assertThat(films.get(2).getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(films.get(2).getMpa()).isEqualTo(new MpaRating(3, "PG-13"));
    }

    @Order(2)
    @Test
    void updateFilm_shouldReturnExistCorrectFilm_afterUpdate() {
        Film film = filmStorage.get(1).get();
        film.setName("TestCheck");
        filmStorage.update(film);

        assertThat(filmStorage.get(1).get().getName()).isEqualTo("TestCheck");
        assertThat(filmStorage.get(1).get().getDescription()).isEqualTo("test");
        assertThat(filmStorage.get(1).get().getReleaseDate()).isNotNull();
        assertThat(filmStorage.get(1).get().getDuration()).isEqualTo(100);
        assertThat(filmStorage.get(1).get().getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(filmStorage.get(1).get().getMpa()).isEqualTo(new MpaRating(3, "PG-13"));
    }

    @Order(3)
    @Test
    void getFilmById_shouldReturnCorrectFilm() {
        filmStorage.add(Film.builder()
                .id(4)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(303)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());

        Film correctFilm = Film.builder()
                .id(4)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(303)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .likes(new HashSet<>())
                .build();

        Film film = filmStorage.get(4).get();
        assertEquals(film, correctFilm);
    }

    @Order(4)
    @Test
    public void removeFilm_shouldReturnTrue() {
        filmStorage.delete(Film.builder().id(1).build());
        filmStorage.delete(Film.builder().id(2).build());
        filmStorage.delete(Film.builder().id(3).build());
        filmStorage.delete(Film.builder().id(4).build());

        assertEquals(filmStorage.get(1), Optional.empty());
        assertEquals(filmStorage.get(2), Optional.empty());
        assertEquals(filmStorage.get(3), Optional.empty());
        assertEquals(filmStorage.get(4), Optional.empty());
    }

    @Test
    void getAllMpa_shouldReturnCorrectListSize() {
        List<MpaRating> mpaRatingList = mpaRatingDao.getAllMpaRatings();
        assertThat(mpaRatingList.size()).isEqualTo(5);
    }

    @Test
    public void getMpaById_shouldReturnCorrectMpa() {
        Optional<MpaRating> mpaRating = mpaRatingDao.getMpaRating(1);
        assertThat(mpaRating.get()).hasFieldOrPropertyWithValue("id", 1);
    }


}