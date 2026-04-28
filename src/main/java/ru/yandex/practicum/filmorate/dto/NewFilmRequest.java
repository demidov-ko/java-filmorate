package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.AfterMinDate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NewFilmRequest {
    @NotBlank
    private String name;

    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    private String description;

    @NotNull
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @AfterMinDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительным числом")
    @NotNull(message = "Продолжительность не может быть пустой")
    private Long duration;

    private Mpa mpa;
    private Set<Genre> genres;
}
