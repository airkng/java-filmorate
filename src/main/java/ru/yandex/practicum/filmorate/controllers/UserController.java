package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUserList();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Integer id) {
        return userService.getUserById(id);
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
        return userService.addUser(user);
    }

    //Метод PUT в данном случае похоже работает только на замену, так как просто при заносе
    // в мапу, тесты выдают ошибку
    @PutMapping
    public User replaceUser(@Valid @RequestBody User user) {
        return userService.replaceUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendsId}")
    public Integer addFriend(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendsId") Integer friendsId
    ) {
        userService.addFriend(id, friendsId);
        return id;
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public Integer deleteFriend(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendsId") Integer friendsId
    ) {
        userService.deleteFriend(id, friendsId);
        return id;
    }


    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleAlreadyExistObject(final ObjectAlreadyExistException e) {
        return new ResponseEntity<>(Map.of("Error: ", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFoundObject(final ObjectNotFoundException e) {
        return new ResponseEntity<>(Map.of("Exception: ", e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
