package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;            // — уникальный идентификатор фильма
    private String name;        //— название
    private String description; //— описание фильма
    private LocalDate releaseDate;//— дата релиза
    private Long duration;   //— продолжительность фильма

}
