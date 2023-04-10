package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})

public class User {
    private HashSet<Integer> friends = new HashSet<>();

    private Integer id;
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "Некорректный логин")
    private String login;

    private String name;
    @PastOrPresent(message = "Некорректная дата рождения")
    private LocalDate birthday;


    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
