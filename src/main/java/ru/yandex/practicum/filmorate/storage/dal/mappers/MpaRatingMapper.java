package ru.yandex.practicum.filmorate.storage.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRatingMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        //TODO: либо присваивать значения полей через сеттеры? Как лучше?
        Integer id = rs.getInt("rating_id");
        String name = rs.getString("rating_name");

        return new MpaRating(id, name);
    }
}
