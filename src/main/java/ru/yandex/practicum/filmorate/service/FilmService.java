package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

//тут будет бизнес-логика

@Slf4j
@RequiredArgsConstructor
@Service
//Добавляет лайки к фильму
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> filmsLike = new HashMap<>();

    //добавляет лайк фильму
    public void addLike(Long filmId, Long userId) {
        log.debug("Попытка поставить лайк фильму с id {} пользователем с id {}", filmId, userId);
        filmStorage.findById(filmId);
        userStorage.findById(userId);

        filmsLike.computeIfAbsent(filmId, id -> new HashSet<>())
                .add(userId);

        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }

    //убирает лайки
    public void removeLike(Long filmId, Long userId) {
        log.debug("Попытка убрать лайк с фильма {} пользователем с id {}", filmId, userId);
        filmStorage.findById(filmId);
        userStorage.findById(userId);

        Set<Long> set = filmsLike.get(filmId);

        if (set != null) {
            set.remove(userId);
            if (set.isEmpty()) {
                filmsLike.remove(filmId);
            }
        }
        log.info("Пользователь с id {} убрал лайк у фильма с id {}", userId, filmId);
    }

    //выводит топ фильмов по лайкам
    public List<Film> getTopFilmsByLikes(int count) {
        log.info("Получаем топ {} фильмов", count);

        return filmsLike.entrySet().stream()
                .sorted((likes1, likes2) ->
                        likes2.getValue().size() - likes1.getValue().size())
                .limit(count)
                .map(entry -> filmStorage.findById(entry.getKey()))
                .toList();
    }
}



