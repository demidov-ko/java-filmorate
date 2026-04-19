package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        FilmRepository.class,
        FilmRowMapper.class,
        UserRepository.class,
        UserRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmRepositoryTest {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    private Film film;

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .name("Film 1")
                .description("Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120L)
                .ratingId(1)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>(Set.of(
                        new Genre(1, "Комедия"),
                        new Genre(2, "Драма")
                )))
                .build();

        filmRepository.save(film);
    }

    @Test
    void shouldSaveFilm() {
        assertThat(film.getId()).isPositive();
        assertThat(film.getGenres()).hasSize(2);
    }

    @Test
    void shouldFindFilmById() {
        Optional<Film> foundFilm = filmRepository.findById(film.getId());

        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(value ->
                        assertThat(value.getName()).isEqualTo("Film 1")
                );
    }

    @Test
    void shouldFindFilmByName() {
        Optional<Film> foundFilm = filmRepository.findByName("Film 1");

        assertThat(foundFilm).isPresent();
    }

    @Test
    void shouldFindAllFilms() {
        List<Film> films = filmRepository.findAll();

        assertThat(films).hasSize(1);
    }

    @Test
    void shouldUpdateFilm() {
        film.setName("Updated Film");

        Film updatedFilm = filmRepository.update(film);

        assertThat(updatedFilm.getName()).isEqualTo("Updated Film");
    }

    @Test
    void shouldAddLike() {
        User user = User.builder()
                .email("user@mail.ru")
                .login("userLogin")
                .name("User")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userRepository.save(user);

        filmRepository.addLike(film.getId(), user.getId());

        Optional<Film> updatedFilm = filmRepository.findById(film.getId());

        assertThat(updatedFilm.get().getUserIds()).contains(user.getId());
    }

    @Test
    void shouldRemoveLike() {
        User user = User.builder()
                .email("user@mail.ru")
                .login("userLogin")
                .name("User")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userRepository.save(user);

        filmRepository.addLike(film.getId(), user.getId());
        filmRepository.removeLike(film.getId(), user.getId());

        Optional<Film> updatedFilm = filmRepository.findById(film.getId());

        assertThat(updatedFilm.get().getUserIds()).doesNotContain(user.getId());
    }

    @Test
    void shouldReturnTopFilms() {
        List<Film> films = filmRepository.getTopFilms(10);

        assertThat(films).hasSize(1);
        assertThat(films.get(0).getName()).isEqualTo("Film 1");
    }
}