package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(MpaRatingDao.class)
class MpaRatingDaoTest {
    private final MpaRatingDao mpaRatingDao;

    @Test
    void testGetById() {
        Optional<MpaRating> rating = mpaRatingDao.getById(1);

        assertThat(rating)
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r).hasFieldOrPropertyWithValue("id", 1));
    }
}
