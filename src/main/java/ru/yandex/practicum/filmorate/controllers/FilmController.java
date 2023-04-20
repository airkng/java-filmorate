package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor

public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilmList();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(value = "id") Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, value = "count", defaultValue = "10") Integer count) {
        return filmService.getCountPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film replaceFilm(@Valid @RequestBody Film film) {
        return filmService.replaceFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
        return id;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
        return id;
    }


}
