package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDao {

    private final JdbcTemplate jdbc;

    public List<Genre> findAll() {
        return jdbc.query("SELECT * FROM genres ORDER BY id", new GenreMapper());
    }

    public Optional<Genre> getById(int id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(
                    "SELECT * FROM genres WHERE id = ?",
                    new GenreMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
