package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        RatingRepository.class,
        RatingRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RatingRepositoryTest {

    private final RatingRepository ratingRepository;

    @Test
    void shouldFindAllRatings() {
        assertThat(ratingRepository.findAll()).hasSize(5);
    }

    @Test
    void shouldFindRatingById() {
        assertThat(ratingRepository.findById(1))
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating.getName()).isEqualTo("G")
                );
    }
}
