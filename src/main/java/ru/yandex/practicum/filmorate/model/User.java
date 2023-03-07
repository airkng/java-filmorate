package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    /**
     * целочисленный идентификатор — id;
     * электронная почта — email;
     * логин пользователя — login;
     * имя для отображения — name;
     * дата рождения — birthday.
     *
     * Как у вас дела, молодой человек? Что нового?
     * Как там продвигается приложение?
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
