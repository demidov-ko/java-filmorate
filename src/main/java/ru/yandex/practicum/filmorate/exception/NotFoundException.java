/**
 * Пакет исключений.
 */
package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение ненайденных данных.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
