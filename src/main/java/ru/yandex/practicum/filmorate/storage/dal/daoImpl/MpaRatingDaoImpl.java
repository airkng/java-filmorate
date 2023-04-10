package ru.yandex.practicum.filmorate.storage.dal.daoImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.dal.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.storage.dal.mappers.MpaRatingMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaRatingDaoImpl implements MpaRatingDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public Optional<MpaRating> getMpaRating(Integer id) {
        //TODO: не знаю, нужно ли в конце sqlQuery ставить ;
        String sqlQuery = "SELECT * FROM mpa_rating WHERE rating_id = ?";
        return jdbcTemplate.query(sqlQuery, new MpaRatingMapper(), id).stream().findAny();
    }

    public List<MpaRating> getAllMpaRatings() {
        String sqlQuery = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, new MpaRatingMapper());
    }
}
