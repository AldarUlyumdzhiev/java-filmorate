package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendDao friendDao;
    private final UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Друг с id=%d не найден", friendId)
                ));
        friendDao.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Друг с id=%d не найден", friendId)
                ));
        friendDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        return friendDao.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        userStorage.getById(otherId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", otherId)
                ));
        return friendDao.getCommonFriends(userId, otherId);
    }
}