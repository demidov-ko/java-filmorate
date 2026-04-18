package ru.yandex.practicum.filmorate.dal;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL = "SELECT * FROM films";

    private static final String FIND_BY_ID = "SELECT * FROM films WHERE id = ?";

    private static final String INSERT = """
            INSERT INTO films(name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?
            WHERE id = ?
            """;

    private static final String FIND_BY_NAME = "SELECT * FROM films WHERE name = ?";

    private static final String ADD_LIKE = "INSERT INTO likes(user_id, film_id) VALUES (?, ?)";

    private static final String REMOVE_LIKE = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

    private static final String GET_LIKES = "SELECT user_id FROM likes WHERE film_id = ?";

    private static final String TOP_FILMS = """
            SELECT f.*
            FROM films AS f
            LEFT JOIN likes AS l ON f.id = l.film_id
            GROUP BY f.id
            ORDER BY COUNT(l.user_id) DESC
            LIMIT ?
            """;

    private static final String ADD_GENRE = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";

    private static final String GET_GENRES = """
            SELECT g.id, g.name
            FROM genres AS g
            JOIN film_genres AS fg ON g.id = fg.genre_id
            WHERE fg.film_id = ?
            """;

    private static final String DELETE_GENRES = "DELETE FROM film_genres WHERE film_id = ?";

    private static final String GET_RATING = "SELECT * FROM mpa WHERE id = ?";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    //добавляет новый фильм в БД
    public Film save(Film film) {
        long id = insert(
                INSERT,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingId()
        );
        film.setId(id);
        //обновление связи с жанрами(удал. старые, доб. новые)
        updateGenres(film);
        loadExtra(film);
        return film;
    }

    //обновляет существующий фильм
    public Film update(Film film) {
        update(
                UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingId(),
                film.getId()
        );
        updateGenres(film);
        loadExtra(film);

        return film;
    }

    //получает все фильмы из таблицы films
    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL);
        //загружаются лайки и жанры для каждого фильма
        films.forEach(this::loadExtra);
        return films;
    }

    //ищет фильм по id
    public Optional<Film> findById(long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID, filmId);
        film.ifPresent(this::loadExtra);
        return film;
    }

    //ищет фильм по названию
    public Optional<Film> findByName(String name) {
        Optional<Film> film = findOne(FIND_BY_NAME, name);
        film.ifPresent(this::loadExtra);
        return film;
    }

    //возвращает топ‑N фильмов по количеству лайков
    public List<Film> getTopFilms(int count) {
        //тут соед.табл. films и likes по film_id, групп. по film.id, сорт. по количеству лайков и огр. по лимиту
        List<Film> films = jdbc.query(TOP_FILMS, mapper, count);
        films.forEach(this::loadExtra);
        return films;
    }

    //добавляет лайк к фильму
    public void addLike(long filmId, long userId) {
        jdbc.update(ADD_LIKE, userId, filmId);

    }

    //удаляет лайк фильма
    public void removeLike(long filmId, long userId) {
        jdbc.update(REMOVE_LIKE, userId, filmId);
    }

    //обновляет связи фильма с жанрами в таблице film_genres
    private void updateGenres(Film film) {
        //Удаляет все существующие связи для данного фильма
        jdbc.update(DELETE_GENRES, film.getId());

        //если у фильма есть жанры, то для каждого жанра выполн. ADD_GENRE
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                jdbc.update(ADD_GENRE, film.getId(), g.getId());
            }
        }
    }

    //загружает доп. инф. для фильма: лайки и жанры
    private void loadExtra(Film film) {
        //получения списка id пользователей, поставивших лайк
        List<Long> likes = jdbc.queryForList(GET_LIKES, Long.class, film.getId());
        //преобразование список в HashSet и установка в поле userIds объекта film
        film.setUserIds(new HashSet<>(likes));

        //получение жанров фильма путем соед. genres и film_genres по genre_id
        List<Genre> genres = jdbc.query(GET_GENRES,
                //для каждой строки создаём объект Genre с id и названием
                (rs, i) -> new Genre(
                        rs.getInt("id"),
                        rs.getString("name")
                ),
                film.getId());

        //преобр. списка жанров в LinkedHashSet и устанавка в поле genres объекта film
        film.setGenres(new LinkedHashSet<>(genres));

        //получение рейтинга
        if (film.getRatingId() > 0) {
            try {
                Mpa mpa = jdbc.queryForObject(
                        GET_RATING,
                        (rs, i) -> new Mpa(rs.getInt("id"), rs.getString("name")),
                        film.getRatingId()
                );
                film.setMpa(mpa);
            } catch (EmptyResultDataAccessException ignored) {
            }
        }
    }
}
