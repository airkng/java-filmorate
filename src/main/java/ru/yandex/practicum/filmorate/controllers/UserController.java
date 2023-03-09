package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UniqueObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    /**
     * Для UserController:
     * создание пользователя;
     * обновление пользователя;
     * получение списка всех пользователей.
     */
    private HashMap<Integer, User> users = new HashMap<>();
    private static int countId = 1;

    @GetMapping
    public Collection<User> getUsers() {
       return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (users.containsValue(user) || users.containsKey(user.getId())) {
            throw new UniqueObjectException("Объект " + user + " уже существует. Воспользуйтесь методом PUT");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        User newUser = user.toBuilder()
                .id(countId++)
                .build();
        users.put(newUser.getId(), newUser);
        return newUser;
    }
    //Метод PUT в данном случае похоже работает только на замену, так как просто при заносе
    // в мапу, тесты выдают ошибку
    @PutMapping
    public User replaceUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ValidateException("Объект " + user + " не найден");
        }
        return user;
    }
}
