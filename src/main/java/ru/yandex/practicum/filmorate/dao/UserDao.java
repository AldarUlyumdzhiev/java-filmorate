package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDao implements UserStorage {

    private final JdbcTemplate jdbc;

    @Override
    public List<User> getAll() {
        return jdbc.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public Optional<User> getById(Long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(
                    "SELECT * FROM users WHERE id = ?",
                    new UserMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        UserValidator.validate(user);
        jdbc.update("INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        Long id = jdbc.queryForObject("SELECT MAX(id) FROM users", Long.class);
        user.setId(id);
        return user;
    }


    @Override
    public User update(User user) {
        UserValidator.validate(user);
        jdbc.update("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
                user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }
}
