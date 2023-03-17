package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.IuserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static int countId = 1;

    private final IuserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.userStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
       return userStorage.getValues();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Integer id) {
        if (!userStorage.containsKey(id)) {
            throw new ObjectNotFoundException("Объект User c " + id + " not found");
        }
        return userStorage.get(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable(value = "id") Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "otherId") Integer otherId
    ) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (userStorage.contains(user)) {
            throw new ObjectAlreadyExistException("Объект " + user + " уже существует. Воспользуйтесь методом PUT");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(countId++);
        userStorage.put(user);
        return user;
    }

    //Метод PUT в данном случае похоже работает только на замену, так как просто при заносе
    // в мапу, тесты выдают ошибку
    @PutMapping
    public User replaceUser(@Valid @RequestBody User user) {
        if (userStorage.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            userStorage.put(user);
        } else {
            throw new ValidateException("Объект " + user + " не найден");
        }
        return user;
    }

    @PutMapping(value = "/{id}/friends/{friendsId}")
    public Integer addFriend(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendsId") Integer friendsId
    ) {
        if (id == null || friendsId == null) {
            throw new ValidateException("Отсутствует переменная пути id = " + id + "friendsId = " + friendsId);
        }
        userService.addFriend(id, friendsId);
        return id;
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public Integer deleteFriend (
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendsId") Integer friendsId
    ) {
    userService.deleteFriend(id, friendsId);
    return id;
    }

    //TODO: переименовать метод
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle (final ObjectAlreadyExistException e) {
        return new ResponseEntity<>(Map.of("Error: ", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFound (final ObjectNotFoundException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
