/**
 * Пакет исключений.
 */
package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение для дублирующихся данных.
 */
public class DuplicatedDataException extends RuntimeException{
    public DuplicatedDataException(String message) {
        super(message);
    }
}
