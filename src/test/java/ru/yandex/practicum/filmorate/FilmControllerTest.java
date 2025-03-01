package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    void throwsExceptionBlankName() {
        Film film = new Film();
        film.setName("");  // Пустое название
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(7200);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );

        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void throwsExceptionDescriptionTooLong() {
        Film film = new Film();
        film.setName("Test Name");
        String longDescription = "s".repeat(201);  // Описание превышает лимит в 200 символов
        film.setDescription(longDescription);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(7200);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );

        assertEquals("Максимальная длина описания фильма - 200 символов", exception.getMessage());
    }

    @Test
    void throwsExceptionReleaseDateTooEarly() {
        Film film = new Film();
        film.setName("Test Name");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // Дата релиза до 28.12.1895
        film.setDuration(7200);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );

        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void throwsExceptionDurationIsZero() {
        Film film = new Film();
        film.setName("Test Name");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);  // Продолжительность = 0

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );

        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }
}
