package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LikeListDaoTest {
    private final LikeListDao likeListDao;
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    @Order(1)
    @Test
    public void addLikeAndRemove_shouldReturnTrueAndCorrectValues() {
        User user = userDao.add(User.builder().name("testing").birthday(LocalDate.now()).login("ttestt").email("example@email.com").build());
        Film film = filmDao.add(Film.builder().name("filmos").description("desc").duration(123).mpa(new MpaRating(1, null)).releaseDate(LocalDate.now()).build());

        assertTrue(likeListDao.addLike(film.getId(), user.getId()));
        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of(user.getId()));
        likeListDao.removeLike(film.getId(), user.getId());

        assertEquals(likeListDao.getLikesFromFilm(film.getId()), List.of());

        userDao.delete(user);
        filmDao.delete(film);

        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1");
    }
}
