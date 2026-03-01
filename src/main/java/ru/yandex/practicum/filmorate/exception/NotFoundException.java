/**
 * Пакет исключений приложения Filmorate.
 */
package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение для случаев, когда ресурс не найден.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
