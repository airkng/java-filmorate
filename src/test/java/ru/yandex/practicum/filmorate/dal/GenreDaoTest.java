package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class GenreDaoTest {
    private final GenreDao genreDao;

    @Test
    public void getAll_shouldReturnAllGenres() {
        List<Genre> genreList = genreDao.getAllGenres();
        List<Genre> correctList = List.of(new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик"));
        assertEquals(genreList, correctList);
    }

    @Test
    public void get_shouldReturnCorrectValue() {
        Optional<Genre> comedy = genreDao.getGenre(1);
        Optional<Genre> triller = genreDao.getGenre(4);
        Optional<Genre> action = genreDao.getGenre(6);
        Optional<Genre> empty = genreDao.getGenre(999);

        assertEquals(comedy.get(), new Genre(1, "Комедия"));
        assertEquals(triller.get(), new Genre(4, "Триллер"));
        assertEquals(action.get(), new Genre(6, "Боевик"));
        assertEquals(empty, Optional.empty());
    }

    @Test
    public void get_shouldReturnEmptyOptional_NonExistGenre() {
        assertEquals(genreDao.getGenre(999), Optional.empty());
    }
}
