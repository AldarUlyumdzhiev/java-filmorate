package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDao.class, MpaRatingDao.class, GenreDao.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoTest {

    private final FilmDao filmDao;
    private final MpaRatingDao mpaRatingDao;

    @Test
    void testCreateAndGetById() {
        MpaRating rating = mpaRatingDao.getById(1).orElseThrow();

        Film film = Film.builder()
                .name("test film")
                .description("test description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(Duration.ofMinutes(120))
                .mpaRating(rating)
                .build();

        Film created = filmDao.create(film);
        Optional<Film> retrieved = filmDao.getById(created.getId());

        assertThat(retrieved)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("name", "test film")
                                .hasFieldOrPropertyWithValue("duration", 120)
                );
    }
}
