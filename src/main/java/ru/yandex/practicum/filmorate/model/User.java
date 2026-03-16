package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id; //Уникальный идентификатор пользователя
    private String email; //Электронная почта пользователя
    private String login; //Логин пользователя
    private String name; //Имя пользователя
    private LocalDate birthday; //Дата рождения пользователя
}
