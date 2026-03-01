/**
 * Пакет исключений.
 */
package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение ошибок валидации.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
