package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(GenreDao.class)
class GenreDaoTest {
    private final GenreDao genreDao;

    @Test
    void testGetById() {
        Optional<Genre> genre = genreDao.getById(1);

        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g).hasFieldOrPropertyWithValue("id", 1));
    }
}
