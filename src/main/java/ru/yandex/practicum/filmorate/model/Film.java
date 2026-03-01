/**
 * Пакет модели.
 */
package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    /**
     * Уникальный идентификатор фильма.
     */
    private Long id;
    /**
     * Название.
     */
    private String name;
    /**
     * Описание фильма.
     */
    private String description;
    /**
     * Дата релиза.
     */
    private LocalDate releaseDate;
    /**
     * Продолжительность фильма.
     */
    private Long duration;
}
