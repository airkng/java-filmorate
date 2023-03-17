package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
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

    public boolean deleteFriend(Integer id) {
        return friends.remove(id);
    }

    public boolean addFriend(Integer id) {
        return friends.add(id);
    }

    public boolean containFriend(Integer id) {
        return friends.contains(id);
    }

    public HashSet<Integer> getFriends() {
        return friends;
    }
}
