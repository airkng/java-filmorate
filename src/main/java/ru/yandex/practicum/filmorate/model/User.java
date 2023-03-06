package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    /**
     * целочисленный идентификатор — id;
     * электронная почта — email;
     * логин пользователя — login;
     * имя для отображения — name;
     * дата рождения — birthday.
     */
    private Integer id;
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "Некорректный логин")
    private String login;

    private String name;
    @PastOrPresent(message = "Некорректная дата рождения")
    private LocalDate birthday;
}
