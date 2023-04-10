package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();


    @Override
    public Film delete(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film put(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film replace(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean containsValue(Film film) {
        return films.containsValue(film);
    }

    @Override
    public boolean containsKey(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Collection<Film> getValues() {
        return films.values();
    }

    @Override
    public Optional<Film> get(Integer id) {
        return Optional.of(films.get(id));
    }


}
