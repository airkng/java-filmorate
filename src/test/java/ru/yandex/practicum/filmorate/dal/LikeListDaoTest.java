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
import ru.yandex.practicum.filmorate.dal.dao.LikeListDao;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class LikeListDaoTest {
    private final LikeListDao likeListDao;
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    private User user = User.builder()
            .id(1)
            .name("testing")
            .birthday(LocalDate.now())
            .login("ttestt")
            .email("example@email.com")
            .build();

    private Film film = Film.builder()
            .id(1)
            .name("filmos")
            .description("desc")
            .duration(123)
            .mpa(new MpaRating(1, null))
            .releaseDate(LocalDate.now())
            .build();

    @BeforeEach
    public void addDataToDb() {
        userDao.add(user);
        filmDao.add(film);
    }

    @AfterEach
    public void deleteDataFromDb() {
        userDao.delete(user);
        filmDao.delete(film);

        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1");
    }

    @Test
    public void addLike_shouldReturnTrueAndCorrectValues() {
        assertTrue(likeListDao.addLike(film.getId(), user.getId()));
        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of(user.getId()));
    }

    @Test
    public void removeLike_shouldReturnCorrectValues() {
        likeListDao.addLike(film.getId(), user.getId());
        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of(user.getId()));

        likeListDao.removeLike(film.getId(), user.getId());
        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of());
    }

    @Test
    void addLike_shouldReturnCorrectValues_addSomeLikes() {
        Film film2 = Film.builder()
                .id(2)
                .name("test2")
                .description("test2")
                .duration(100)
                .mpa(new MpaRating(2, null))
                .releaseDate(LocalDate.now())
                .build();

        User user2 = User.builder()
                .id(2)
                .name("user2")
                .birthday(LocalDate.now())
                .login("user2")
                .email("user2@email.com")
                .build();
        userDao.add(user2);
        filmDao.add(film2);

        likeListDao.addLike(film.getId(), user.getId());
        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of(user.getId()));
        likeListDao.addLike(film.getId(), user2.getId());
        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of(user.getId(), user2.getId()));
        likeListDao.addLike(film2.getId(), user2.getId());
        assertEquals(likeListDao.getLikesFromFilm(film2.getId()), List.of(user2.getId()));

        likeListDao.deleteAllLikesFromFilm(film2.getId());
        likeListDao.deleteAllLikesFromFilm(film.getId());
        userDao.delete(user2);
        filmDao.delete(film2);
    }

}
