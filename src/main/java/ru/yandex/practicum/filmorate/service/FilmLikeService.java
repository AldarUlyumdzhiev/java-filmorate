package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmLikeService {
    private final Map<Long, Set<Long>> filmsLikes = new HashMap<>();
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmLikeService(
            InMemoryFilmStorage filmStorage,
            InMemoryUserStorage userStorage
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long id, Long userId) {
        filmStorage.validateFilm(filmStorage.getById(id));
        userStorage.validateUser(userStorage.getById(userId));

        // null значения уже учитываются
        Set<Long> filmLikes = filmsLikes.get(id);
        filmLikes.add(userId);
    }

    public void removeLike(Long id, Long userId) {
        filmStorage.validateFilm(filmStorage.getById(id));
        userStorage.validateUser(userStorage.getById(userId));

        filmsLikes.get(id).remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmsLikes.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))  // Сортируем по убыванию
                .limit(count)
                .map(entry -> filmStorage.getById(entry.getKey()))
                .collect(Collectors.toList());
    }

    // Доп метод для инициализации сета лайкнувших фильм пользователей
    public void addFilmToLikeMap(Long id) {
        filmsLikes.put(id, new HashSet<>());
    }
}
