package ru.yandex.practicum.filmorate.storage.dal.daoImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dal.dao.FriendshipDao;

@Repository
@RequiredArgsConstructor

public class FriendshipDaoImpl implements FriendshipDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean deleteAllFriendsFromUser(Integer id) {
        String sqlQuery =
                "DELETE FROM friends_list " +
                        "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return true;
    }

    @Override
    public boolean deleteFriendFromUser(Integer userId, Integer friendId) {
        String sqlQuery =
                "DELETE FROM friends_list " +
                        "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return true;
    }

    @Override
    public boolean addFriend(Integer userId, Integer friendId) {
        String sqlQuery =
                "INSERT INTO friends_list(user_id, friend_id, status_id) " +
                        "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, ACCEPTED_FRIEND_STATUS);

        String sqlQueryForFriend = "INSERT INTO friends_list(user_id, friend_id, status_id) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQueryForFriend, friendId, userId, UNACCEPTED_FRIEND_STATUS);
        return true;
    }
}
