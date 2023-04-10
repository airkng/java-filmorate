package ru.yandex.practicum.filmorate.storage.dal.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> getGenre(Integer id);

    List<Genre> getAllGenres();

    Genre[] addGenreToFilm(Genre[] genres, Integer filmId);

    boolean deleteGenresFromFilm(Integer id);
}
