package ru.yandex.practicum.filmorate.storage.dal.daoImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dal.dao.LikeListDao;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeListDaoImpl implements LikeListDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Override
    public boolean deleteAllLikesFromFilm(Integer id) {
        String sqlForLikeList = "DELETE FROM like_list WHERE film_id = ?";
        jdbcTemplate.update(sqlForLikeList, id);
        return true;
    }

    @Override
    public boolean deleteAllUserLikes(Integer id) {
        String sqlForLikeList = "DELETE FROM like_list WHERE user_id = ?";
        jdbcTemplate.update(sqlForLikeList, id);
        return true;
    }

    @Override
    public List<Integer> getLikesFromFilm(Integer id) {
        List<Integer> likes = new ArrayList<>();
        String sqlQuery = "SELECT user_id " +
                "FROM like_list " +
                "WHERE film_id = ?";
         SqlRowSet row = jdbcTemplate.queryForRowSet(sqlQuery, id);
         while(row.next()) {
             likes.add(row.getInt("user_id"));
         }
         return likes;
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO like_list(film_id, user_id) " +
                "VALUES (?, ?) ";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return true;
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM like_list " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return true;

    }
}
