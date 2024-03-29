package ru.yandex.practicum.filmorate.dal.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

    User delete(User user);

    User add(User user);

    User update(User user);

    boolean containsValue(User user);

    boolean containsKey(Integer id);

    Collection<User> getValues();

    Optional<User> get(Integer id);
}
