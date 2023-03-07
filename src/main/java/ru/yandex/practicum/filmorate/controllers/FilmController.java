package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    /**
     * Я считаю, надо создать петицию, чтобы всех PM-ов уволили и вообще удалили такую профессию и оставить бэкендеров
     * и девопсов. Ну ладно, может быть фронтендеров
     */
    private HashMap<Integer, Film> films = new HashMap<>();
    private static int countId = 1;

    @GetMapping()
    public List<Film> getFilms() {
        List<Film> filmsList = new ArrayList<>();
        for (Film film : films.values()) {
            filmsList.add(film);
        }
        return filmsList;
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        checkFilmReleaseDate(film);
        if (films.containsKey(film.getId()) || films.containsValue(film)) {
            log.warn("Повторный запрос на добавление через метод POST {}", film);
            throw new ValidateException("Объект " + film + " уже существует. Воспользуйтесь методом PUT");
        }
        Film newFilm = film.toBuilder()
                .id(countId++)
                .build();
        films.put(newFilm.getId(), newFilm);
        log.debug("Добавление объекта методом POST" + film);
        return newFilm;
        //TODO: Изначально тут оборачивал в try-catch исключение, которые обрабатывал принтом стектрейса
        // но у меня вылезала ошибка при тестировании, так как статус респонса был 200, а надо было 500 либо 400
        // Это всегда так, если исключение обрабатываешь, то статус 200 приходит в ответ? Я потому что попытался исправить
        // это добавлением @ResponseStatus к исключению, но УВЫ
    }

    @PutMapping()
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
