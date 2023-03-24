package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Builder(toBuilder = true)
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private final HashSet<Integer> likes = new HashSet<>();

    private Integer id;
    @NotBlank(message = "Пустое поле name")
    private String name;
    @Size(message = "Превышен лимит знаков поля description " + MAX_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    public boolean deleteLike(Integer id) {
        return likes.remove(id);
    }

    public boolean addLike(Integer id) {
        return likes.add(id);
    }

    public boolean containLike(Integer id) {
        return likes.contains(id);
    }

    public HashSet<Integer> getLikes() {
        return likes;
    }
}
