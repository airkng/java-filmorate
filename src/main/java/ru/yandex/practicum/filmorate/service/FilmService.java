package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.*;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UniqueObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final MpaRatingDao mpaRatingDao;
    private final GenreDao genreDao;
    private final LikeListDao likeListDao;

    public List<Film> getCountPopularFilms(Integer count) {
        if (count < 0) {
            throw new ValidateException("Variable count is negative or null: " + count);
        }
        Collection<Film> filmSet = filmDao.getValues();
        return filmSet.stream()
                .sorted((f1, f2) -> -1 * (f1.getLikes().size() - f2.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> getFilmList() {
        return filmDao.getValues().stream().sorted(Comparator.comparingInt(Film::getId)).collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        return filmDao.get(id).orElseThrow(() -> {
            throw new ObjectNotFoundException("Object Film id = " + id + " not found");
        });
    }

    public Film addFilm(Film film) {
        checkFilmReleaseDate(film);
        film.setGenres(checkForGenresDuplicates(film.getGenres()));
        if (filmDao.containsValue(film)) {
            log.warn("Повторный запрос на добавление через метод POST {}", film);
            throw new UniqueObjectException("Объект " + film + " уже существует. Воспользуйтесь методом PUT");
        }
        return filmDao.add(film);
    }

    public Film replaceFilm(Film film) {
        checkFilmReleaseDate(film);
        film.setGenres(checkForGenresDuplicates(film.getGenres()));
        if (!filmDao.containsKey(film.getId())) {
            log.warn("Не существующий объект {} метод PUT", film);
            throw new ObjectNotFoundException(film + "не существует. Воспользуйтесь методом Post");
        }
        return filmDao.update(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        if (!filmDao.containsKey(filmId)) {
            throw new ObjectNotFoundException("Film " + filmId + " not found");
        }
        if (!userDao.containsKey(userId)) {
            throw new ObjectNotFoundException("User " + userId + " not found");
        }
        likeListDao.removeLike(filmId, userId);
        likeListDao.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        //TODO: попробовал удалить 2 условия ниже. Тесты из требуют, чтобы при удалении
        // лайка от несушествующего пользователя он выводил 404 Not found -_-
        if (!userDao.containsKey(userId)) {
            throw new ObjectNotFoundException("User " + userId + " not found");
        }
        likeListDao.removeLike(filmId, userId);
    }

    //MpaRating methods

    public MpaRating getMpaRating(Integer id) {
        Optional<MpaRating> mpa = mpaRatingDao.getMpaRating(id);
        if (mpa.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Рейтинг с id = %d не найден", id));
        }
        return mpa.get();
    }

    public Collection<MpaRating> getAllMpaRatings() {
        List<MpaRating> mpaList = mpaRatingDao.getAllMpaRatings();
        if (mpaList.isEmpty()) {
            throw new ObjectNotFoundException("Рейтинги не были найдены");
        }
        return mpaList;
    }

    //Genres methods
    public Genre getGenre(Integer id) {
        Optional<Genre> genre = genreDao.getGenre(id);
        if (genre.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Жанр с id=%d не найден", id));
        }
        return genre.get();
    }

    public Collection<Genre> getAllGenres() {
        List<Genre> genreList = genreDao.getAllGenres();
        if (genreList.isEmpty()) {
            throw new ObjectNotFoundException("Жанры не были найдены");
        }
        return genreList;
    }

    private static boolean checkFilmReleaseDate(Film film) {
        final LocalDate borderDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(borderDate)) {
            throw new ValidateException("Дата релиза Film раньше " + borderDate);
        }
        return true;
    }

    private static List<Genre> checkForGenresDuplicates(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return List.of();
        }
        TreeSet<Genre> set = new TreeSet<>(genres);

        return new ArrayList<>(set);
    }
}
