package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Попытка обновления пользователя с id = null");
            throw new ConditionsNotMetException("id пользователя должен быть указан. Получено id = null");
        }
        if (!users.containsKey(newUser.getId())) {
            log.error("Попытка обновления несуществующего пользователя с id = {}", newUser.getId());
            throw new NotFoundException(String.format("Пользователь с id = %d не найден.", newUser.getId()));
        }
        validateUser(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Обновлен пользователь с id = {}: {}", newUser.getId(), newUser);
        return newUser;
    }

    // Доп метод для валидации полей User
    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Ошибка валидации: пустая электронная почта");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Ошибка валидации: некорректный email {}", user.getEmail());
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: дата рождения в будущем {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    // Доп метод для генерации нового id
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
