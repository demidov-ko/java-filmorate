/**
 * Пакет исключений приложения Filmorate.
 */
package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение для ошибок валидации данных.
 */
public class ValidationException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     */
    public ValidationException(final String message) {
        super(message);
    }
}
