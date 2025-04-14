package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendDao {

    private final JdbcTemplate jdbc;

    public void addFriend(Long userId, Long friendId) {
        jdbc.update(
                "MERGE INTO friends KEY(user_id, friend_id) VALUES (?, ?)",
                userId, friendId
        );
    }

    public void removeFriend(Long userId, Long friendId) {
        int ownDeleted = jdbc.update(
                "DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
                userId, friendId
        );

        if (ownDeleted > 0) {
            jdbc.update(
                    "DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
                    friendId, userId
            );
        }
    }

    public List<User> getFriends(Long userId) {
        String sql = """
            SELECT u.*
              FROM users   u
              JOIN friends f ON u.id = f.friend_id
             WHERE f.user_id = ?
            """;
        return jdbc.query(sql, new UserMapper(), userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        String sql = """
            SELECT u.*
              FROM users   u
              JOIN friends f1 ON u.id = f1.friend_id
              JOIN friends f2 ON u.id = f2.friend_id
             WHERE f1.user_id = ? AND f2.user_id = ?
            """;
        return jdbc.query(sql, new UserMapper(), userId, otherId);
    }
}
