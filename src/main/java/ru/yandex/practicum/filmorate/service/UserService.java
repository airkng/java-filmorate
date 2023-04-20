package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;
    private final FriendshipDao friendshipDao;

    public Collection<User> getUserList() {
        return userDao.getValues().stream().sorted(Comparator.comparingInt(User::getId)).collect(Collectors.toList());
    }

    public User getUserById(Integer id) {
        return userDao.get(id).orElseThrow(() -> {
            throw new ObjectNotFoundException("Объект User c " + id + " not found");
        });
    }

    public User addUser(User user) {
        if (userDao.containsValue(user)) {
            throw new ObjectAlreadyExistException("Объект " + user + " уже существует. Воспользуйтесь методом PUT");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userDao.add(user);
    }

    public User replaceUser(User user) {
        if (userDao.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return userDao.update(user);
        } else {
            throw new ObjectNotFoundException("Объект " + user + " не найден");
        }
    }

    public void addFriend(Integer userId, Integer friendsId) {
        Optional<User> userOptional = userDao.get(userId);
        Optional<User> friendOptional = userDao.get(friendsId);
        if (userOptional.isPresent() && friendOptional.isPresent()) {
            if (userOptional.get().getFriends() != null && userOptional.get().getFriends().contains(friendsId)) {
                throw new ObjectAlreadyExistException("Object " + friendOptional + " already add in friends");
            }
            if (friendOptional.get().getFriends() != null && friendOptional.get().getFriends().contains(userId)) {
                throw new ObjectAlreadyExistException("Object " + userOptional + " already add in friends");
            }
            friendshipDao.addFriend(userId, friendsId);
        } else {
            throw new ObjectNotFoundException("Объект User с индексом " + userId + " или " + friendsId + " не найден");
        }

    }

    public void deleteFriend(Integer id, Integer friendId) {
        Optional<User> userOptional = userDao.get(id);
        Optional<User> friendOptional = userDao.get(friendId);
        if (userOptional.isPresent() && friendOptional.isPresent()) {
            friendshipDao.deleteFriendFromUser(id, friendId);
        } else {
            throw new ObjectNotFoundException("Объект User с индексом " + id + " или " + friendId + " не найден");
        }

    }

    public Collection<User> getFriends(Integer id) {
        if (userDao.containsKey(id)) {
            List<User> userList = new ArrayList<>();
            HashSet<Integer> friendIdList = userDao.get(id).get().getFriends();
            if (friendIdList.isEmpty()) {
                return List.of();
            }
            for (Integer userId : friendIdList) {
                userList.add(userDao.get(userId).get());
            }
            return userList;
        } else {
            throw new ObjectNotFoundException("Object User with id = " + id + " not found");
        }
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        Optional<User> userOptional = userDao.get(id);
        Optional<User> friendOptional = userDao.get(otherId);

        if (userOptional.isEmpty() && friendOptional.isEmpty()
                || (userOptional.get().getFriends().isEmpty() || friendOptional.get().getFriends().isEmpty())
        ) {
            return List.of();
        } else {
            List<User> commonFriends = new ArrayList<>();
            HashSet<Integer> otherUserFriendsSet = friendOptional.get().getFriends();

            for (Integer userId : userOptional.get().getFriends()) {
                if (otherUserFriendsSet.contains(userId)) {
                    commonFriends.add(userDao.get(userId).get());
                }
            }
            return commonFriends;
        }


    }
}
