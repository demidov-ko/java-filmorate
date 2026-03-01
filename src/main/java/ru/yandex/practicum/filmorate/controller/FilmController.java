/**
 * Пакет контроллеров приложения Filmorate.
 */
package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Контроллер для работы с фильмами.
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    /**
     * Хранилище фильмов в памяти.
     */
    private final Map<Long, Film> films = new HashMap<>();
    /**
     * Минимальная допустимая дата релиза фильма (28 декабря 1895 года).
     */
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    /**
     * Максимальная длина описания 200 символов.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    /**
     * Создаёт новый фильм.
     */
    @PostMapping
    public final Film create(@RequestBody final Film film) {
        log.info("Создание нового фильма: {}", film.getName());

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Попытка создать фильм с пустым названием");
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Попытка добавить описание длиной более 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        // Проверка даты релиза
        if (film.getReleaseDate() == null) {
            log.warn("Попытка создать фильм без даты релиза");
            throw new ValidationException("Дата релиза должна быть указана");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Попытка создать фильм с релизом до 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        // Продолжительность фильма должна быть положительным числом
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.warn("Попытка создать фильм с некорректной продолжительностью");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.debug(
                "Фильм {} успешно создан с ID: {}",
                film.getName(),
                film.getId());

        return film;
    }

    /**
     * Редактирует пользователя.
     */
    @PutMapping
    public Film update(@RequestBody final Film newFilm) {
        log.info("Редактирование фильма ID={}: {}", newFilm.getId(), newFilm.getName());

        if (newFilm.getId() == null) {
            log.warn("Попытка обновить фильм без id");
            throw new ValidationException("id должен быть указан");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Попытка найти фильм с несуществующим id");
            throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
        }
        Film oldFilm = films.get(newFilm.getId());

        // Проверяем и обновляем название
        if (newFilm.getName() != null) {
            if (newFilm.getName().isBlank()) {
                log.warn("Попытка обновить фильм с пустым названием");
                throw new ValidationException("Название не может быть пустым");
            }
            oldFilm.setName(newFilm.getName());
        }
        // Проверяем и обновляем описание
        if (newFilm.getDescription() != null) {
            if (newFilm.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
                log.warn("Попытка обновить описание длиной более 200 символов");
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            oldFilm.setDescription(newFilm.getDescription());
        }
        // Проверяем и обновляем дату релиза
        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                log.warn("Попытка обновить фильм с релизом до 28 декабря 1895 года");
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        // Проверяем и обновляем продолжительность
        if (newFilm.getDuration() != null) {
            if (newFilm.getDuration() <= 0) {
                log.warn("Попытка обновить фильм с некорректной продолжительностью");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
            oldFilm.setDuration(newFilm.getDuration());
        }
        log.debug("Фильм ID={} успешно обновлен. Изменения: название={}, описание={}, " +
                        "дата релиза={}, продолжительность={}",
                oldFilm.getId(),
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration());

        return oldFilm;
    }

    /**
     * Получаем всех пользователей.
     */
    @GetMapping
    public Collection<Film> findAll() {
        log.trace("Запрос всех фильмов, найдено: {}", films.size());

        return films.values();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
