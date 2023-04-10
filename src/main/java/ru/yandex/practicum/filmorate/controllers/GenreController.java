package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        return filmService.getGenre(id);
    }

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }
}
