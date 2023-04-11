package ru.yandex.practicum.filmorate.dal.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dao.GenreDao;
import ru.yandex.practicum.filmorate.dal.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getGenre(Integer genreId) {
        String sqlQuery = "SELECT * FROM genre WHERE genre_id=?";
        return jdbcTemplate.query(sqlQuery, new GenreMapper(), genreId).stream().findAny();
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, new GenreMapper());
    }

    @Override
    public Genre[] addGenreToFilm(Genre[] genres, Integer filmId) {
        if (genres == null) {
            return new Genre[]{};
        }
        List<Genre> fromDb = new ArrayList<>();
        String sqlQuery =
                "INSERT INTO film_genres(genre_id, film_id) " +
                        "VALUES (?, ?) ";
        for (int i = 0; i < genres.length; i++) {
            Genre genre = genres[i];
            jdbcTemplate.update(sqlQuery, genre.getId(), filmId);
            Optional<Genre> genreOptional = getGenre(genre.getId());
            genreOptional.ifPresent(fromDb::add);
        }
        return fromDb.toArray(genres);
    }

    @Override
    public boolean deleteGenresFromFilm(Integer filmId) {
        String sqlForFilmGenres = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlForFilmGenres, filmId);
        return true;
    }
}
