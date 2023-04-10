package ru.yandex.practicum.filmorate.storage.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dal.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dal.dao.LikeListDao;
import ru.yandex.practicum.filmorate.storage.dal.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.storage.dal.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.dal.mappers.GenreMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final MpaRatingDao mpaRatingDao;
    private final LikeListDao likeListDao;

    @Override
    public Film delete(Film film) {
        likeListDao.deleteAllLikesFromFilm(film.getId());
        genreDao.deleteGenresFromFilm(film.getId());

        String sqlQuery = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return film;
    }

    @Override
    public Film put(Film film) {
        String sqlQuery = "INSERT INTO film(name, description, release_date, duration, rating_id)" +
                "VALUES(?, ?, ?, ?, ?) ";
        KeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update((con) -> {
            PreparedStatement prst = con.prepareStatement(sqlQuery,
                    Statement.RETURN_GENERATED_KEYS);
            prst.setString(1, film.getName());
            prst.setString(2, film.getDescription());
            prst.setDate(3, Date.valueOf(film.getReleaseDate()));
            prst.setInt(4, film.getDuration());
            prst.setInt(5, film.getMpa().getId());
            return prst;
        }, kh);
        film.setId((Integer) kh.getKey());
        Optional<MpaRating> mpaRatingOptional = mpaRatingDao.getMpaRating(film.getMpa().getId());

        if (mpaRatingOptional.isPresent()) {
            film.setMpa(mpaRatingOptional.get());
        }

        if (film.getGenres() != null) {
            film.setGenres(genreDao.addGenreToFilm(film.getGenres(), film.getId()));
        }
        return film;
    }

    @Override
    public Film replace(Film film) {
        genreDao.deleteGenresFromFilm(film.getId());

        String sqlQuery = "UPDATE film " +
                "SET name = ?, release_date = ?, description = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, film.getName(), Date.valueOf(film.getReleaseDate()), film.getDescription(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        genreDao.addGenreToFilm(film.getGenres(), film.getId());
        return film;
    }

    @Override
    public boolean containsValue(Film film) {
        String sqlQuery =
                "SELECT * " +
                        "FROM film as f " +
                        "WHERE name = ? AND description = ? AND release_date = ? AND duration= ?";

        Optional<Film> filmOptional = jdbcTemplate.query(sqlQuery, new FilmMapper(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration()).stream().findAny();
        if (filmOptional.isEmpty()) {
            return false;
        } else {
            return true;
        }


    }

    @Override
    public boolean containsKey(Integer id) {
        String sqlQuery =
                "SELECT * " +
                        "FROM film as f " +
                        "WHERE film_id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<Film> getValues() {
        List<Film> filmsList = new ArrayList<>();
        String sqlQueryFindFilm =
                "SELECT film_id " +
                        "FROM film AS f";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQueryFindFilm);
        while (sqlRowSet.next()) {
            Integer filmId = sqlRowSet.getInt("film_id");
            Optional<Film> film = get(filmId);
            if (film.isPresent()) {
                filmsList.add(film.get());
            }
        }
        return filmsList;
    }

    @Override
    public Optional<Film> get(Integer id) {
        String sqlQuery = "" +
                "SELECT * FROM film " +
                "WHERE film_id=?";
        Optional<Film> filmOptional = jdbcTemplate.query(sqlQuery, new FilmMapper(), id).stream().findAny();
        if (filmOptional.isPresent()) {
            Optional<MpaRating> mpaRatingOptional = mpaRatingDao.getMpaRating(filmOptional.get().getMpa().getId());
            mpaRatingOptional.ifPresent(mpaRating -> filmOptional.get().setMpa(mpaRating));
            String sqlQueryFindGenres =
                    "SELECT * " +
                            "FROM film_genres AS fg " +
                            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                            "WHERE film_id = ?";

            Genre[] genres = jdbcTemplate.query(sqlQueryFindGenres, new GenreMapper(), id).toArray(new Genre[]{});
            filmOptional.get().setGenres(genres);

            filmOptional.get().setLikes(new HashSet<>(likeListDao.getLikesFromFilm(id)));
            return filmOptional;
        }
        return Optional.empty();
    }

}
