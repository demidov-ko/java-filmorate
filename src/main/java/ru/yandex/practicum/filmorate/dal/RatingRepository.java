package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepository extends BaseRepository<Mpa> {
    private static final String FIND_ALL = "SELECT * FROM mpa ORDER BY id";
    private static final String FIND_BY_ID = "SELECT * FROM mpa WHERE id = ?";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL);
    }

    public Optional<Mpa> findById(long id) {
        return findOne(FIND_BY_ID, id);
    }
}

