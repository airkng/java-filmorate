package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        String date = rs.getString("release_date");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //TODO: ошибка в преобразоваии даты
        // Пытался сделать так, но вызывается UnsupportedOperationException, хз что не так
        // LocalDate releaseDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate releaseDate = LocalDate.parse(date, timeFormatter);
        String name = rs.getString("name");
        String description = rs.getString("description");

        Integer duration = rs.getInt("duration");
        Integer filmId = rs.getInt("film_id");
        Integer ratingId = rs.getInt("rating_id");
        MpaRating mpa = new MpaRating(ratingId, null);
        return new Film().toBuilder()
                .id(filmId)
                .description(description)
                .name(name)
                .duration(duration)
                .releaseDate(releaseDate)
                .mpa(mpa).build();
    }
}
