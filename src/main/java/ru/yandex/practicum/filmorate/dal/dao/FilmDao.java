package ru.yandex.practicum.filmorate.dal.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmDao {
    Film delete(Film film);

    Film add(Film film);

    Film update(Film film);

    boolean containsValue(Film film);

    boolean containsKey(Integer id);

    Collection<Film> getValues();

    Optional<Film> get(Integer id);
}
