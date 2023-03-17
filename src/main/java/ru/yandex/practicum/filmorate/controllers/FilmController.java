package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.UniqueObjectException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.IfilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static int countId = 1;

    private final IfilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.filmStorage = inMemoryFilmStorage;
        this.filmService = filmService;

    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getValues();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(value = "id") Integer id) {
        if (!filmStorage.containsKey(id)) {
            throw new ObjectNotFoundException("Object Film id = " + id + " not found");
        }
        return filmStorage.get(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, value = "count", defaultValue = "10") Integer count) {
        if (count < 0) {
            throw new ValidateException("Variable count is negative or null: " + count);
        }
        return filmService.getCountPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkFilmReleaseDate(film);
        if (filmStorage.contains(film)) {
            log.warn("Повторный запрос на добавление через метод POST {}", film);
            throw new UniqueObjectException("Объект " + film + " уже существует. Воспользуйтесь методом PUT");
        }
        //Убрал билдер, чето бессмысленный он тут
        film.setId(countId++);
        filmStorage.put(film);
        return film;
    }

    //Метод PUT в данном случае похоже работает только на замену, так как просто при заносе
    // в мапу, тесты выдают ошибку
    @PutMapping
    public Film replaceFilm(@Valid @RequestBody Film film) {
        checkFilmReleaseDate(film);
        if (!filmStorage.containsKey(film.getId())) {
            log.warn("Не существующий объект {} метод PUT", film);
            throw new ValidateException(film + "не существует. Воспользуйтесь методом Post");
        }
        filmStorage.put(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null || userId == null) {
            throw new ValidateException("Отсутствует переменная пути id = " + id + "userId = " + userId);
        }
        filmService.addLike(id, userId);
        return id;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
        return id;
    }

    private static boolean checkFilmReleaseDate(Film film) throws ValidateException {
        final LocalDate borderDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(borderDate)) {
            throw new ValidateException("Дата релиза Film раньше " + borderDate);
        }
        return true;
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFound(final ObjectNotFoundException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleBadRequest(final ObjectAlreadyExistException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
