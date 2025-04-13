package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeDao likeDao;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {
        filmStorage.getById(filmId);
        userStorage.getById(userId);
        likeDao.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.getById(filmId);
        userStorage.getById(userId);
        likeDao.removeLike(filmId, userId);
    }
}
