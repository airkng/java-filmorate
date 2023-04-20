package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")

public class MpaRatingController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public MpaRating getMpaRating(@PathVariable Integer id) {
        return filmService.getMpaRating(id);
    }

    @GetMapping
    public Collection<MpaRating> getAllMpaRatings() {
        return filmService.getAllMpaRatings();
    }
}
