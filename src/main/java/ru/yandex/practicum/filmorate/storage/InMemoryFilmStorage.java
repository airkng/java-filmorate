package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements IfilmStorage {
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
    public boolean contains(Film film) {
        if (films.containsKey(film.getId()) || films.containsValue(film)) {
            return true;
        } else {
            return false;
        }
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
    public Film get(Integer id) {
        return films.get(id);
    }


}
