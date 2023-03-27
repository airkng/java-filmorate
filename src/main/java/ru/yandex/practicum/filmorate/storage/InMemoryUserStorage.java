package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User delete(User user) {
       users.remove(user.getId());
       return user;
    }

    @Override
    public User put(User user) {
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
    public User get(Integer id) {
        return users.get(id);
    }


}
