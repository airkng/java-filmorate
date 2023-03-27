package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = {"id"})
public class User {
    private final HashSet<Integer> friends = new HashSet<>();

    private Integer id;
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "Некорректный логин")
    private String login;

    private String name;
    @PastOrPresent(message = "Некорректная дата рождения")
    private LocalDate birthday;

    public HashSet<Integer> getFriends() {
        return friends;
    }
}
