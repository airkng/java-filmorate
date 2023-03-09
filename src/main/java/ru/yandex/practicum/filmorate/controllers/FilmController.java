package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UniqueObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();
    private static int countId = 1;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkFilmReleaseDate(film);
        if (films.containsKey(film.getId()) || films.containsValue(film)) {
            log.warn("Повторный запрос на добавление через метод POST {}", film);
            throw new UniqueObjectException("Объект " + film + " уже существует. Воспользуйтесь методом PUT");
        }
        Film newFilm = film.toBuilder()
                .id(countId++)
                .build();
        films.put(newFilm.getId(), newFilm);
        log.debug("Добавление объекта методом POST" + film);
        return newFilm;

    }

    //Метод PUT в данном случае похоже работает только на замену, так как просто при заносе
    // в мапу, тесты выдают ошибку
    @PutMapping
    public Film replaceFilm(@Valid @RequestBody Film film) {
        checkFilmReleaseDate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.warn("Не существующий объект {} метод PUT", film);
            throw new ValidateException(film.toString() + "не существует. Воспользуйтесь методом Post");
        }
        return film;
    }

    private static boolean checkFilmReleaseDate(Film film) throws ValidateException {
        final LocalDate borderDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(borderDate)) {
            throw new ValidateException("Дата релиза Film раньше " + borderDate);
        }
        return true;
    }
}
