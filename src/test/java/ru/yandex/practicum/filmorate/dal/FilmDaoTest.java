package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.dao.FilmDao;
import ru.yandex.practicum.filmorate.dal.dao.MpaRatingDao;
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

public class FilmDaoTest {
    private final FilmDao filmDao;
    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingDao mpaRatingDao;


    @BeforeEach
    public void addFilmsForTest() {
        filmDao.add(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());
        filmDao.add(Film.builder()
                .name("test1")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());
        filmDao.add(Film.builder()
                .name("test2")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MpaRating(3, "PG-13"))
                .build());
    }

    @AfterEach
    public void removeFilms() {
        filmDao.delete(Film.builder().id(1).build());
        filmDao.delete(Film.builder().id(2).build());
        filmDao.delete(Film.builder().id(3).build());

        jdbcTemplate.update("ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1");
    }


    @Test
    void getAllFilms_shouldReturnCorrectList_afterPutFilms() throws SQLException {

        List<Film> films = (List<Film>) filmDao.getValues();

        assertThat(films.get(0).getName()).isEqualTo("test");
        assertThat(films.get(1).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getId()).isEqualTo(3);
        assertThat(films.get(2).getName()).isEqualTo("test2");
        assertThat(films.get(2).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getReleaseDate()).isNotNull();
        assertThat(films.get(2).getDuration()).isEqualTo(100);
        assertThat(films.get(2).getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(films.get(2).getMpa()).isEqualTo(new MpaRating(3, "PG-13"));
    }


    @Test
    void updateFilm_shouldReturnExistCorrectFilm_afterUpdate() {
        Film film = filmDao.get(1).get();
        film.setName("TestCheck");
        filmDao.update(film);

        assertThat(filmDao.get(1).get().getName()).isEqualTo("TestCheck");
        assertThat(filmDao.get(1).get().getDescription()).isEqualTo("test");
        assertThat(filmDao.get(1).get().getReleaseDate()).isNotNull();
        assertThat(filmDao.get(1).get().getDuration()).isEqualTo(100);
        assertThat(filmDao.get(1).get().getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(filmDao.get(1).get().getMpa()).isEqualTo(new MpaRating(3, "PG-13"));
    }

    @Test
    void updateOneMoreFilm_shouldReturnCorrectFilm_afterUpdate() {
        Film film = Film.builder()
                .id(2)
                .name("TESTING")
                .mpa(new MpaRating(1, "G"))
                .duration(123)
                .description("desc")
                .releaseDate(LocalDate.now())
                .likes(new HashSet<>())
                .genres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")))
                .build();
        filmDao.update(film);
        assertEquals(film, filmDao.get(2).get());
    }


    @Test
    void getFilmById_shouldReturnCorrectFilm() {
        filmDao.add(Film.builder()
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

        Film film = filmDao.get(4).get();
        assertEquals(film, correctFilm);
    }


    @Test
    public void removeFilm_shouldReturnTrue() {
        filmDao.delete(Film.builder().id(1).build());
        filmDao.delete(Film.builder().id(2).build());
        filmDao.delete(Film.builder().id(3).build());


        assertEquals(filmDao.get(1), Optional.empty());
        assertEquals(filmDao.get(2), Optional.empty());
        assertEquals(filmDao.get(3), Optional.empty());

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

    @Test
    public void getMpaById_NonExistMpa_shouldReturnEmpty() {
        Optional<MpaRating> mpaRating = mpaRatingDao.getMpaRating(999);
        assertEquals(mpaRating, Optional.empty());
    }

}