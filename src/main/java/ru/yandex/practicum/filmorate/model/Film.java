package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private HashSet<Integer> likes = new HashSet<>();

    private Integer id;
    @NotBlank(message = "Пустое поле name")
    private String name;
    @Size(message = "Превышен лимит знаков поля description " + MAX_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private List<Genre> genres;
    private MpaRating mpa;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, MpaRating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}
