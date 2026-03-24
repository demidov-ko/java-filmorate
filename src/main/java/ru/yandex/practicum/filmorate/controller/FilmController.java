package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    //Создаёт новый фильм
    @PostMapping
    public final Film create(@RequestBody final Film film) {
        return filmService.create(film);
    }

    //Редактирует фильм
    @PutMapping
    public Film update(@RequestBody final Film newFilm) {
        return filmService.update(newFilm);
    }

    //Получаем все фильмы
    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) {
        return filmService.findById(id);
    }

    //---------------------------------------------------------------------
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilmsByLikes(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilmsByLikes(count);
    }

}
