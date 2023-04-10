package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dal.dao.FriendshipDao;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipDao friendshipDao;

    public Collection<User> getUserList() {
        return userStorage.getValues().stream().sorted(Comparator.comparingInt(User::getId)).collect(Collectors.toList());
    }

    public Optional<User> getUserById(Integer id) {
        Optional<User> user = userStorage.get(id);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Объект User c " + id + " not found");
        }
        return user;
    }

    public User addUser(User user) {
        if (userStorage.containsValue(user)) {
            throw new ObjectAlreadyExistException("Объект " + user + " уже существует. Воспользуйтесь методом PUT");
        }
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
            userStorage.replace(user);
        } else {
            throw new ObjectNotFoundException("Объект " + user + " не найден");
        }
        return user;
    }

    public void addFriend(Integer userId, Integer friendsId) {

        if (userStorage.containsKey(userId) && userStorage.containsKey(friendsId)) {
            Optional<User> userOptional = userStorage.get(userId);
            Optional<User> friendOptional = userStorage.get(friendsId);
            User user = userOptional.get();
            User friend = friendOptional.get();

            if (user.getFriends() != null && user.getFriends().contains(friendsId)) {
                throw new ObjectAlreadyExistException("Object " + friend + " already add in friends");
            }
            if (friend.getFriends() != null && friend.getFriends().contains(userId)) {
                throw new ObjectAlreadyExistException("Object " + user + " already add in friends");
            }
            friendshipDao.addFriend(userId, friendsId);
        } else {
            throw new ObjectNotFoundException("Объект User с индексом " + userId + " или " + friendsId + " не найден");
        }
    }

    public void deleteFriend(Integer id, Integer friendId) {
        Optional<User> userOptional = userStorage.get(id);
        Optional<User> friendOptional = userStorage.get(friendId);
        if (userOptional.isPresent() && friendOptional.isPresent()) {
            friendshipDao.deleteFriendFromUser(id, friendId);
        } else {
            throw new ObjectNotFoundException("Объект User с индексом " + id + " или " + friendId + " не найден");
        }

    }

    public Collection<User> getFriends(Integer id) {
        if (userStorage.containsKey(id)) {
            List<User> userList = new ArrayList<>();
            HashSet<Integer> friendIdList = userStorage.get(id).get().getFriends();
            if (friendIdList.isEmpty()) {
                return List.of();
            }
            for (Integer userId : friendIdList) {
                userList.add(userStorage.get(userId).get());
            }
            return userList;
        } else {
            throw new ObjectNotFoundException("Object User with id = " + id + " not found");
        }
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        if (userStorage.containsKey(id) && userStorage.containsKey(otherId)) {
            if (userStorage.get(id).get().getFriends().isEmpty() || userStorage.get(otherId).get().getFriends().isEmpty()) {
                return List.of();
            }
            List<User> commonFriends = new ArrayList<>();
            HashSet<Integer> otherUserFriendsSet = userStorage.get(otherId).get().getFriends();

            for (Integer userId : userStorage.get(id).get().getFriends()) {
                if (otherUserFriendsSet.contains(userId)) {
                    commonFriends.add(userStorage.get(userId).get());
                }
            }
            return commonFriends;
        } else {
            return List.of();
        }
    }
}
