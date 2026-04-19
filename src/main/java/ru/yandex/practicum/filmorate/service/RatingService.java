package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public List<Mpa> getRatings() {
        return ratingRepository.findAll();
    }

    public Mpa getRatingById(int id) {
        return ratingRepository.findById(id).orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
    }
}
