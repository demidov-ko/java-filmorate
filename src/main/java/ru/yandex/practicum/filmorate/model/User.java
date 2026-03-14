package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;
    /**
     * Электронная почта пользователя.
     */
    private String email;
    /**
     * Логин пользователя.
     */
    private String login;
    /**
     * Имя пользователя.
     */
    private String name;
    /**
     * Дата рождения пользователя.
     */
    private LocalDate birthday;
}
