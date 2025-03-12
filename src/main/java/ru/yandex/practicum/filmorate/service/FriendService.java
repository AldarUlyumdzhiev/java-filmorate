package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendService {
    private final Map<Long, Set<Long>> usersFriendIds = new HashMap<>();
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FriendService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getFriends(Long id) {
        log.debug("Запрос списка друзей для пользователя id = {}", id);

        userStorage.validateUser(userStorage.getById(id));

        return usersFriendIds.getOrDefault(id, new HashSet<>()).stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        log.debug("Поиск общих друзей между id={} и id={}", id, otherId);

        userStorage.validateUser(userStorage.getById(id));
        userStorage.validateUser(userStorage.getById(otherId));

        Set<Long> firstUserFriends = usersFriendIds.getOrDefault(id, Collections.emptySet());
        Set<Long> secondUserFriends = usersFriendIds.getOrDefault(otherId, Collections.emptySet());

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Добавление в друзья: id={} и id={}", id, friendId);

        userStorage.validateUser(userStorage.getById(id));
        userStorage.validateUser(userStorage.getById(friendId));

        Set<Long> userFriends = usersFriendIds.get(id);
        userFriends.add(friendId);  // Добавляем второго юзера в друзья к первому юзеру

        Set <Long> friendFriends = usersFriendIds.get(friendId);
        friendFriends.add(id);  // Добавляем первого юзера в друзья ко второму юзеру
    }

    public void deleteFriend(Long id, Long friendId) {
        log.info("Удаление из друзей: id = {} и id = {}", id, friendId);

        userStorage.validateUser(userStorage.getById(id));
        userStorage.validateUser(userStorage.getById(friendId));

        Set<Long> userFriends = usersFriendIds.get(id);
        if (userFriends.contains(friendId)) {
            userFriends.remove(friendId);
            Set<Long> friendFriends = usersFriendIds.get(friendId);
            friendFriends.remove(id);
        }
    }

    // Доп метод для инициализации сета друзей нового пользователя
    public void addUserToFriendMap(Long userId) {
        log.debug("Добавление нового пользователя id={} в систему друзей", userId);

        usersFriendIds.put(userId, new HashSet<>());
    }
}
