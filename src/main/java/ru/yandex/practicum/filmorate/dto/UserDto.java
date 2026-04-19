package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

//UserDTO — используется для отображения полной информации о пользователе. Этот класс применяется в ответах
// на запросы данных о пользователе и включает все поля, требуемые для работы с пользователями.

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
