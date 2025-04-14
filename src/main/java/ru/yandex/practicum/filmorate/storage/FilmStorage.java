package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public Collection<Film> getAll();

    Optional<Film> getById(Long id);

    public Film create(Film film);

    public Film update(Film newFilm);

    List<Film> getPopularFilms(int count);
}
