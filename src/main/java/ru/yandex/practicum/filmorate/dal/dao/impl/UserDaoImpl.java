package ru.yandex.practicum.filmorate.dal.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.dal.dao.LikeListDao;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.dal.mappers.FriendMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final LikeListDao likeListDao;
    private final FriendshipDao friendshipDao;

    @Override
    public User delete(User user) {
        likeListDao.deleteAllUserLikes(user.getId());
        friendshipDao.deleteAllFriendsFromUser(user.getId());

        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
        return user;
    }

    @Override
    public User put(User user) {
        String sqlQuery = "INSERT INTO users(email, login, nickname, birthday)" +
                "VALUES(?, ?, ?, ?) ";
        KeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update((con) -> {
            PreparedStatement prst = con.prepareStatement(sqlQuery,
                    Statement.RETURN_GENERATED_KEYS);
            prst.setString(1, user.getEmail());
            prst.setString(2, user.getLogin());
            prst.setString(3, user.getName());
            prst.setDate(4, Date.valueOf(user.getBirthday()));
            return prst;
        }, kh);
        user.setId((Integer) kh.getKey());
        return user;
    }

    @Override
    public User replace(User user) {
        String sqlQuery = "UPDATE users " +
                "SET email = ?, login = ?, nickname = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }

    @Override
    public boolean containsValue(User user) {
        String sqlQuery =
                "SELECT * " +
                        "FROM users as u " +
                        "WHERE email = ? AND login = ? AND nickname = ? AND birthday= ?";

        Optional<User> userOptional = jdbcTemplate.query(sqlQuery, new UserMapper(), user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday()).stream().findAny();
        if (userOptional.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean containsKey(Integer id) {
        String sqlQuery =
                "SELECT * " +
                        "FROM users as u " +
                        "WHERE user_id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<User> getValues() {
        List<User> userList = new ArrayList<>();
        String sqlQueryFindFilm =
                "SELECT user_id " +
                        "FROM users AS u";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQueryFindFilm);
        while (sqlRowSet.next()) {
            Integer userId = sqlRowSet.getInt("user_id");
            Optional<User> user = get(userId);
            if (user.isPresent()) {
                userList.add(user.get());
            }
        }
        return userList;
    }

    @Override
    public Optional<User> get(Integer userId) {
        String sqlQuery = "" +
                "SELECT * FROM users " +
                "WHERE user_id=?";
        Optional<User> userOptional = jdbcTemplate.query(sqlQuery, new UserMapper(), userId).stream().findAny();

        String sqlFindFriends = "SELECT friend_id " +
                "FROM friends_list " +
                "WHERE user_id = ? AND status_id = ?";
        Integer acceptedFriendStatus = 1;
        List<Integer> friendsIdList = jdbcTemplate.query(sqlFindFriends, new FriendMapper(), userId, acceptedFriendStatus);
        if (userOptional.isPresent()) {
            userOptional.get().setFriends(new HashSet<>(friendsIdList));
        }
        return userOptional;
    }
}
