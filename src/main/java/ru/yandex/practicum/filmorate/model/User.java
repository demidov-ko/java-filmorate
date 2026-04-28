package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id; //Уникальный идентификатор пользователя
    private String email; //Электронная почта пользователя
    private String login; //Логин пользователя
    private String name; //Имя пользователя
    private LocalDate birthday; //Дата рождения пользователя

    @Builder.Default
    private Set<Long> friendIds = new HashSet<>();      //список друзей
}
