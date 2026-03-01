/**
 * Пакет исключений приложения Filmorate.
 */
package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение для дублирующихся данных.
 */
public class DuplicatedDataException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     */
    public DuplicatedDataException(final String message) {
        super(message);
    }
}
