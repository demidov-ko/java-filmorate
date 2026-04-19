package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        GenreRepository.class,
        GenreRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreRepositoryTest {

    private final GenreRepository genreRepository;

    @Test
    void shouldFindAllGenres() {
        assertThat(genreRepository.findAll()).hasSize(6);
    }

    @Test
    void shouldFindGenreById() {
        assertThat(genreRepository.findById(1))
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre.getName()).isEqualTo("Комедия")
                );
    }
}
