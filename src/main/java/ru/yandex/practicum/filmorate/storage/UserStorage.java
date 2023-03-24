package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User delete(User user);
    User put(User user);
    boolean containsValue(User user);
    boolean containsKey(Integer id);
    Collection<User> getValues();
    User get(Integer id);
}
