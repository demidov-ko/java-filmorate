package ru.yandex.practicum.filmorate.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    // название ошибки
    String error;
    // подробное описание
    String description;

    @Override
    public String toString() {
        return "ErrorResponse{error='" + error + "', description='" + description + "'}";
    }
}
