package ru.yandex.practicum.filmorate.dal.dao;

import java.util.List;

public interface LikeListDao {
    boolean deleteAllLikesFromFilm(Integer filmId);

    boolean deleteAllUserLikes(Integer userId);

    List<Integer> getLikesFromFilm(Integer id);

    boolean addLike(Integer filmId, Integer userId);

    boolean removeLike(Integer filmId, Integer userId);
}
