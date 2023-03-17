package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.IfilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.IuserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final IfilmStorage filmStorage;
    private final IuserStorage userStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = userStorage;

    }

    public List<Film> getCountPopularFilms(Integer count) {
        Collection<Film> filmSet = filmStorage.getValues();
        return filmSet.stream()
                .sorted((f1, f2) -> -1 * (f1.getLikes().size() - f2.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
    //TODO: Насколько помню, считается не очень хорошим делать пробросы. Но я хочу обработать эти исключения в контроллере
    // или можно здесь ниже написать хендлер и обработать?
    public void addLike(Integer filmId, Integer userId) throws ObjectNotFoundException, ObjectAlreadyExistException {
        if (!filmStorage.containsKey(filmId)) {
            throw new ObjectNotFoundException("Film " + filmId + " not found");
        }
        if (!userStorage.containsKey(userId)) {
            throw new ObjectNotFoundException("User " + userId + " not found");
        }
        Film film = filmStorage.get(filmId);
        if (film.containLike(userId)) {
            throw new ObjectAlreadyExistException("Like from " + userId + " already add");
        }
        film.addLike(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) throws ObjectNotFoundException {
        if (!filmStorage.containsKey(filmId)) {
            throw new ObjectNotFoundException("Film " + filmId + " not found");
        }
        if (!userStorage.containsKey(userId)) {
            throw new ObjectNotFoundException("User " + userId + " not found");
        }
        Film film = filmStorage.get(filmId);
        film.deleteLike(userId);
    }

}
