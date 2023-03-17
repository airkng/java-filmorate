package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements IuserStorage {
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
    public boolean contains(User user) {
        if (users.containsKey(user.getId()) || users.containsValue(user)) {
            return true;
        } else {
            return false;
        }
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
