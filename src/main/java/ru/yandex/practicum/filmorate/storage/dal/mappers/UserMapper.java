package ru.yandex.practicum.filmorate.storage.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String nickname = rs.getString("nickname");

        String date = rs.getString("birthday");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(date, timeFormatter);

        return new User(userId, email, login, nickname, birthday);
    }
}
