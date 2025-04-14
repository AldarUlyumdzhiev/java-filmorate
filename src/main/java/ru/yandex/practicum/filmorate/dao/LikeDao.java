package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeDao {

    private final JdbcTemplate jdbc;

    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO user_like (film_id, user_id) VALUES (?, ?)";
        jdbc.update(sql, filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM user_like WHERE film_id = ? AND user_id = ?";
        jdbc.update(sql, filmId, userId);
    }

    public int getLikesCount(Long filmId) {
        String sql = "SELECT COUNT(*) FROM user_like WHERE film_id = ?";
        return jdbc.queryForObject(sql, Integer.class, filmId);
    }
}
