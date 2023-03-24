package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.UniqueObjectException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class FilmService {
    private static int countId = 1;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getCountPopularFilms(Integer count) {
        if (count < 0) {
            throw new ValidateException("Variable count is negative or null: " + count);
        }
        Collection<Film> filmSet = filmStorage.getValues();
        return filmSet.stream()
                .sorted((f1, f2) -> -1 * (f1.getLikes().size() - f2.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> getFilmList() {
        return filmStorage.getValues();
    }

    public Film getFilmById(Integer id) {
        if (!filmStorage.containsKey(id)) {
            throw new ObjectNotFoundException("Object Film id = " + id + " not found");
        }
        return filmStorage.get(id);
    }

    public Film addFilm(Film film) {
        checkFilmReleaseDate(film);
        if (filmStorage.containsValue(film)) {
            log.warn("Повторный запрос на добавление через метод POST {}", film);
            throw new UniqueObjectException("Объект " + film + " уже существует. Воспользуйтесь методом PUT");
        }
        film.setId(countId++);
        filmStorage.put(film);
        return film;
    }

    public Film replaceFilm(Film film) {
        checkFilmReleaseDate(film);
        if (!filmStorage.containsKey(film.getId())) {
            log.warn("Не существующий объект {} метод PUT", film);
            throw new ValidateException(film + "не существует. Воспользуйтесь методом Post");
        }
        filmStorage.put(film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) throws ObjectNotFoundException, ObjectAlreadyExistException {
        if (filmId == null || userId == null) {
            throw new ValidateException("Отсутствует переменная пути id = " + filmId + "userId = " + userId);
        }
        if (!filmStorage.containsKey(filmId)) {
            throw new ObjectNotFoundException("Film " + filmId + " not found");
        }
        if (!userStorage.containsKey(userId)) {
            throw new ObjectNotFoundException("User " + userId + " not found");
        }
        Film film = filmStorage.get(filmId);
        if (film.containLike(userId)) {
            throw new ObjectAlreadyExistException("Like from " + userId + " already add");
        }
        film.addLike(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) throws ObjectNotFoundException {
        if (!filmStorage.containsKey(filmId)) {
            throw new ObjectNotFoundException("Film " + filmId + " not found");
        }
        if (!userStorage.containsKey(userId)) {
            throw new ObjectNotFoundException("User " + userId + " not found");
        }
        Film film = filmStorage.get(filmId);
        film.deleteLike(userId);
    }

    private static boolean checkFilmReleaseDate(Film film) throws ValidateException {
        final LocalDate borderDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(borderDate)) {
            throw new ValidateException("Дата релиза Film раньше " + borderDate);
        }
        return true;
    }

}
