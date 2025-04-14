package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaRatingDao {

    private final JdbcTemplate jdbc;

    public List<MpaRating> findAll() {
        return jdbc.query("SELECT * FROM mpa_rating ORDER BY id", new MpaRatingMapper());
    }

    public Optional<MpaRating> getById(int id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(
                    "SELECT * FROM mpa_rating WHERE id = ?",
                    new MpaRatingMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
