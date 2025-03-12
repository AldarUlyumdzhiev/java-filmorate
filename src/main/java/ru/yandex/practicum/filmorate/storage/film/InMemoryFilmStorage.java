package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikeService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final FilmLikeService filmLikeService;

    @Autowired
    public InMemoryFilmStorage(@Lazy FilmLikeService filmLikeService) {
        this.filmLikeService = filmLikeService;
    }

    public Collection<Film> getAll() {
        log.debug("Получение всех фильмов. Текущее количество: {}", films.size());
        return films.values();
    }

    public Film getById(Long id) {
        log.debug("Запрос фильма по id: {}", id);
        return films.values().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Фильм с id = {} не найден", id);
                    return new NotFoundException(String.format("Фильм с id = %d не найден", id));
                });
    }

    public Film create(Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        filmLikeService.addFilmToLikeMap(film.getId());
        log.info("Создан новый фильм: {}", film);
        return film;
    }

    public Film update(Film newFilm) {
        log.debug("Обновление фильма: {}", newFilm);
        if (newFilm.getId() == null) {
            log.error("Попытка обновления фильма с id = null");
            throw new ConditionsNotMetException("id фильма должен быть указан, получено id = null");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.error("Попытка обновления несуществующего фильма с id = {}", newFilm.getId());
            throw new NotFoundException(String.format("Фильм с id = %d не найден", newFilm.getId()));
        }
        validateFilm(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Обновлен фильм с id = {}: {}", newFilm.getId(), newFilm);
        return newFilm;
    }

    // Доп метод для валидации полей Film
    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации: пустое название");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Ошибка валидации: описание слишком длинное");
            throw new ValidationException("Максимальная длина описания фильма - 200 символов");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.error("Ошибка валидации: дата релиза {} раньше 28 декабря 1895 года", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == 0 || film.getDuration() <= 0) {
            log.error("Ошибка валидации: продолжительность фильма {} должна быть положительной", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    // Доп метод для генерации нового id
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
