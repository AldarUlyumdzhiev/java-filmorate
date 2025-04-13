package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDao implements FilmStorage {

    private final JdbcTemplate jdbc;

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbc.query("SELECT * FROM films", new FilmMapper());
        films.forEach(f -> {
            loadGenres(f);
            loadMpaRating(f);
        });
        return films;
    }

    @Override
    public Optional<Film> getById(Long id) {
        try {
            Film film = jdbc.queryForObject("SELECT * FROM films WHERE id = ?", new FilmMapper(), id);
            loadGenres(film);
            loadMpaRating(film);
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Film create(Film film) {
        MpaRating rating = findMpaOrThrow(film.getMpaRating().getId());

        jdbc.update("""
                INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
                VALUES (?, ?, ?, ?, ?)
                """,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                rating.getId());

        Long id = jdbc.queryForObject("SELECT MAX(id) FROM films", Long.class);
        film.setId(id);

        insertGenres(film);
        film.setMpaRating(rating);

        return film;
    }

    @Override
    public Film update(Film film) {
        getById(film.getId()).orElseThrow(
                () -> new NotFoundException("Фильм id=" + film.getId() + " не найден"));

        MpaRating rating = findMpaOrThrow(film.getMpaRating().getId());

        jdbc.update("""
                UPDATE films
                   SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?
                 WHERE id = ?
                """,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                rating.getId(),
                film.getId());

        jdbc.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
        insertGenres(film);
        film.setMpaRating(rating);

        return film;
    }

    public List<Film> getPopularFilms(int count) {
        String sql = """
            SELECT f.*
              FROM films f
              LEFT JOIN user_like ul ON f.id = ul.film_id
             GROUP BY f.id
             ORDER BY COUNT(ul.user_id) DESC
             LIMIT ?
            """;

        List<Film> films = jdbc.query(sql, new FilmMapper(), count);
        films.forEach(f -> {
            loadGenres(f);
            loadMpaRating(f);
        });
        return films;
    }

    private MpaRating findMpaOrThrow(int id) {
        try {
            return jdbc.queryForObject(
                    "SELECT * FROM mpa_rating WHERE id = ?",
                    new MpaRatingMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("MPA rating id=" + id + " не найден");
        }
    }


    private void loadGenres(Film film) {
        String sql = "SELECT g.id, g.name FROM genres g JOIN film_genre fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbc.query(sql, new GenreMapper(), film.getId());
        film.setGenres(new HashSet<>(genres));
    }

    private void insertGenres(Film film) {
        if (film.getGenres() == null) return;

        for (Genre g : film.getGenres()) {
            int exists = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM genres WHERE id = ?", Integer.class, g.getId());
            if (exists == 0) {
                throw new NotFoundException("Жанр id=" + g.getId() + " не найден");
            }
            jdbc.update(
                    "MERGE INTO film_genre KEY(film_id, genre_id) VALUES (?, ?)",
                    film.getId(), g.getId());
        }
    }


    private void loadMpaRating(Film film) {
        String sql = "SELECT * FROM mpa_rating WHERE id = ?";
        MpaRating rating = jdbc.queryForObject(sql, new MpaRatingMapper(), film.getMpaRating().getId());
        film.setMpaRating(rating);
    }
}

