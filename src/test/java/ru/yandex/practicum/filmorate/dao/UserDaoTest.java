package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(UserDao.class)
class UserDaoTest {

    private final UserDao userDao;

    @Test
    void testCreateAndFindById() {
        User user = User.builder()
                .email("test@test.ru")
                .login("testlogin")
                .name("test test")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User created = userDao.create(user);
        Optional<User> retrieved = userDao.getById(created.getId());

        assertThat(retrieved)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", created.getId())
                                .hasFieldOrPropertyWithValue("email", "test@test.ru")
                );
    }
}
