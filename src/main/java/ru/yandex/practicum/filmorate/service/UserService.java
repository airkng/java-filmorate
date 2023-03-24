package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private static int countId = 1;
    private final UserStorage userStorage;

    public Collection<User> getUserList() {
        return userStorage.getValues();
    }

    public User getUserById(Integer id) {
        if (!userStorage.containsKey(id)) {
            throw new ObjectNotFoundException("Объект User c " + id + " not found");
        }
        return userStorage.get(id);
    }

    public User addUser(User user) {
        if (userStorage.containsValue(user)) {
            throw new ObjectAlreadyExistException("Объект " + user + " уже существует. Воспользуйтесь методом PUT");
        }
        user.setId(countId++);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userStorage.put(user);
        return user;
    }

    public User replaceUser(User user) {
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

    public void addFriend(Integer id, Integer friendsId) throws ObjectNotFoundException, ObjectAlreadyExistException {
        if (id == null || friendsId == null) {
            throw new ValidateException("Отсутствует переменная пути id = " + id + "friendsId = " + friendsId);
        }
        if (userStorage.containsKey(id) && userStorage.containsKey(friendsId)) {
            User user = userStorage.get(id);
            User friend = userStorage.get(friendsId);

            if (user.containFriend(friendsId)) {
                throw new ObjectAlreadyExistException("Object " + friend + " already add in friends");
            }
            if (friend.containFriend(id)) {
                throw new ObjectAlreadyExistException("Object " + user + " already add in friends");
            }

            user.addFriend(friend.getId());
            friend.addFriend(user.getId());
        } else {
            throw new ObjectNotFoundException("Объект User с индексом " + id + " или " + friendsId + " не найден");
        }
    }

    public void deleteFriend(Integer id, Integer friendId) throws ObjectNotFoundException {
        if (userStorage.containsKey(id) && userStorage.containsKey(friendId)) {
            User user = userStorage.get(id);
            User friend = userStorage.get(friendId);

            user.deleteFriend(friendId);
            friend.deleteFriend(id);
        } else {
            throw new ObjectNotFoundException("Объект User с индексом " + id + " или " + friendId + " не найден");
        }

    }

    public Collection<User> getFriends(Integer id) throws ObjectNotFoundException {
        if (userStorage.containsKey(id)) {
            List<User> userList = new ArrayList<>();
            for (Integer userId : userStorage.get(id).getFriends()) {
                userList.add(userStorage.get(userId));
            }
            return userList;
        } else {
            throw new ObjectNotFoundException("Object User with id = " + id + " not found");
        }
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        if (userStorage.containsKey(id) && userStorage.containsKey(otherId)) {
            if (userStorage.get(id).getFriends().isEmpty() || userStorage.get(otherId).getFriends().isEmpty()) {
                return List.of();
            }
            List<User> commonFriends = new ArrayList<>();
            HashSet<Integer> otherUserFriendsSet = userStorage.get(otherId).getFriends();

            for (Integer userId : userStorage.get(id).getFriends()) {
                if (otherUserFriendsSet.contains(userId)) {
                    commonFriends.add(userStorage.get(userId));
                }
            }
            return commonFriends;
        } else {
            return List.of();
        }
    }
}
