package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private Integer id;
    @NotBlank(message = "Пустое поле name")
    private String name;
    @Size(message = "Превышен лимит знаков поля description " + MAX_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;


}
