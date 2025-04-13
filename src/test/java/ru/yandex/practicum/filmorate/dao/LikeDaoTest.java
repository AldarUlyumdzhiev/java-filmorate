package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDao.class, FilmDao.class, LikeDao.class})
class LikeDaoTest {

    private final UserDao userDao;
    private final FilmDao filmDao;
    private final LikeDao likeDao;

    @Test
    void testAddLikeAndGetPopularFilms() {
        User user = User.builder()
                .email("like@like.ru")
                .login("like")
                .name("like")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();
        Long userId = userDao.create(user).getId();


        MpaRating rating = new MpaRating(1, "PG-13");

        Film film = Film.builder()
                .name("test film")
                .description("desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(Duration.ofMinutes(120))
                .mpaRating(rating)
                .build();
        Long filmId = filmDao.create(film).getId();

        likeDao.addLike(filmId, userId);

        List<Film> popular = filmDao.getPopularFilms(10);
        assertThat(popular).isNotEmpty();
        assertThat(popular.getFirst().getId()).isEqualTo(filmId);
    }
}
