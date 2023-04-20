package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class InMemoryUserDao implements UserDao {
    private HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User delete(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean containsValue(User user) {
        return users.containsValue(user);
    }

    @Override
    public boolean containsKey(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public Collection<User> getValues() {
        return users.values();
    }

    @Override
    public Optional<User> get(Integer id) {
        return Optional.of(users.get(id));
    }


}
