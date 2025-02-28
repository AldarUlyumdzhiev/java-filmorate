package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private final UserController userController = new UserController();

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        User user = new User();
        user.setEmail(""); // Пустой email
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );

        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    void testExceptionInvalidEmail() {
        User user = new User();
        user.setEmail("test.mail.ru"); // Без символа @
        user.setLogin("testLogin");
        user.setName("Test Login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );

        assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    void testExceptionBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.now().plusDays(1)); // День рождения в будущем

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void exceptionUserNotFound() {
        User user = new User();
        user.setId(999L); // Несуществующий id
        user.setEmail("test@mail.ru");
        user.setLogin("testLogin");
        user.setName("Test Login");
        user.setBirthday(LocalDate.of(1995, 1, 1));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userController.update(user)
        );

        assertEquals("Пользователь с id = 999 не найден.", exception.getMessage());
    }
}
