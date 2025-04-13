package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final FriendService friendService;

    // friendService нужен только в методе create(User user), чтобы добавить ему пустой список друзей
    // @Lazy нужна, чтобы избежать циклическую зависимость, тк в FriendService нужен userStorage
    @Autowired
    public InMemoryUserStorage(@Lazy FriendService friendService) {
        this.friendService = friendService;
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User getById(Long id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                    String.format("Пользователь с id = %d не найден", id)
                ));
    }

    public User create(User user) {
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        friendService.addUserToFriendMap(user.getId());
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    public User update(User newUser) {
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
            log.error("Ошибка валидации: некорректный email: {}", user.getEmail());
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: дата рождения в будущем: {}", user.getBirthday());
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
