package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id; //Уникальный идентификатор фильма
    private String name; //Название
    private String description; //Описание фильма
    private LocalDate releaseDate; //Дата релиза
    private Long duration; //Продолжительность фильма

    @Builder.Default
    private Set<Long> userIds = new HashSet<>();    //пользватели, которым поставилои лайки

    private long ratingId;
    private Mpa mpa;

    @Builder.Default
    private Set<Genre> genres = new LinkedHashSet<>();
}


