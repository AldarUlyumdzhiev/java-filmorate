package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FriendController {
    private static final String USER_FRIENDS_PATH = "/{id}/friends";
    private static final String USER_FRIEND_PATH = USER_FRIENDS_PATH + "/{friend-id}";

    private final FriendService friendService;

    @PutMapping(USER_FRIEND_PATH)
    public void addFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        friendService.addFriend(id, friendId);
    }

    @DeleteMapping(USER_FRIEND_PATH)
    public void deleteFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        friendService.removeFriend(id, friendId);
    }

    @GetMapping(USER_FRIENDS_PATH)
    public List<User> getFriends(@PathVariable Long id) {
        return friendService.getFriends(id);
    }

    @GetMapping(USER_FRIENDS_PATH + "/common/{other-id}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable("other-id") Long otherId) {
        return friendService.getCommonFriends(id, otherId);
    }
}
