package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface IfilmStorage {
    Film delete(Film film);
    Film put(Film film);
    boolean contains(Film film);
    boolean containsKey(Integer id);
    Collection<Film> getValues();
    Film get(Integer id);
}
