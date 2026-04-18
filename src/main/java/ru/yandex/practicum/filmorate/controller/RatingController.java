package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {

    private final RatingRepository ratingRepository;

    @GetMapping
    public List<Mpa> getAll() {
        return ratingRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
    }
}
