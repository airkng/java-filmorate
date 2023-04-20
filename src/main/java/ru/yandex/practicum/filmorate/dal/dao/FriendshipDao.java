package ru.yandex.practicum.filmorate.dal.dao;

public interface FriendshipDao {
    public static final Integer ACCEPTED_FRIEND_STATUS = 1;
    public static final Integer UNACCEPTED_FRIEND_STATUS = 0;

    boolean deleteAllFriendsFromUser(Integer id);

    boolean deleteFriendFromUser(Integer userId, Integer friendId);

    boolean addFriend(Integer userId, Integer friendId);
}
