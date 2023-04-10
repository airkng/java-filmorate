package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film delete(Film film);
    Film put(Film film);
    Film replace(Film film);
    boolean containsValue(Film film);
    boolean containsKey(Integer id);
    Collection<Film> getValues();
    Optional<Film> get(Integer id);
}
