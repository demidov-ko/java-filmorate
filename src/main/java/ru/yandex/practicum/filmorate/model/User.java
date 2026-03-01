package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;                //— уникальный идентификатор пользователя
    private String email;           //— электронная почта пользователя
    private String login;        //— login пользователя
    private String name;        //— имя пользователя
    private LocalDate birthday; //— дата рождения пользователя
}
