package ru.yandex.practicum.filmorate.dal.dao;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaRatingDao {
    List<MpaRating> getAllMpaRatings();

    Optional<MpaRating> getMpaRating(Integer id);
}
