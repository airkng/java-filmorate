package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

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

}
