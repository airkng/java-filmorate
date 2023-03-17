package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.unchecked.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.IuserStorage;

import java.util.*;

@Service
public class UserService {
    private final IuserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    //Принцип действия работы с эксепшенами до конца не ясен
    //Допустим, у меня хендлер обрабатывает исключение ObjectNotFoundException в контроллере
    //TODO: Мне надо пробрасывать его через throws как я это сделал ниже?

    public void addFriend(Integer id, Integer friendsId) throws ObjectNotFoundException, ObjectAlreadyExistException {
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

            for(Integer userId : userStorage.get(id).getFriends()) {
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
