package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data

public class Film {
    private Long id; //Уникальный идентификатор фильма
    private String name; //Название
    private String description; //Описание фильма
    private LocalDate releaseDate; //Дата релиза
    private Long duration; //Продолжительность фильма
}
