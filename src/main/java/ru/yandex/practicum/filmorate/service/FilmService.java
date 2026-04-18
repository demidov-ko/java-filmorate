package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;

    public FilmDto createFilm(NewFilmRequest request) {
// Проверка MPA
        if (request.getMpa() != null) {
            ratingRepository.findById(request.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
        }
// Проверка жанров
        if (request.getGenres() != null) {
            for (Genre genre : request.getGenres()) {
                genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр не найден"));
            }
        }
        Film film = FilmMapper.mapToFilm(request);
        return FilmMapper.mapToFilmDto(filmRepository.save(film));
    }


    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film film = filmRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        if (request.getMpa() != null) {
            ratingRepository.findById(request.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
        }
        if (request.getGenres() != null) {
            for (Genre genre : request.getGenres()) {
                genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр не найден"));
            }
        }

        FilmMapper.updateFilmFields(film, request);
        return FilmMapper.mapToFilmDto(filmRepository.update(film));
    }

    //метод возвращает список DTO‑объектов, готовых для отправки клиенту
    public List<FilmDto> getFilms() {
        return filmRepository.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto getFilmById(long id) {
        return filmRepository.findById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + id));
    }

    public void addLike(long filmId, long userId) {
        filmRepository.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        filmRepository.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmRepository.findById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        filmRepository.removeLike(filmId, userId);
    }

    public List<FilmDto> getTopFilms(int count) {
        return filmRepository.getTopFilms(count)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
