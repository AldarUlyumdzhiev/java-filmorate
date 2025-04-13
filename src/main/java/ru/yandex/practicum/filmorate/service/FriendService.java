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
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException("User " + friendId + " not found"));
        friendDao.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException("User " + friendId + " not found"));
        friendDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        return friendDao.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        userStorage.getById(otherId)
                .orElseThrow(() -> new NotFoundException("User " + otherId + " not found"));
        return friendDao.getCommonFriends(userId, otherId);
    }
}