package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class FriendController {
    private final FriendService friendService;
    private static final String FRIEND_PATH = "/{id}/friends/{friend-id}";

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        return friendService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{other-id}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable("other-id") Long otherId) {
        return friendService.getCommonFriends(id, otherId);
    }

    @PutMapping(FRIEND_PATH)
    public void addFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        friendService.addFriend(id, friendId);
    }

    @DeleteMapping(FRIEND_PATH)
    public void deleteFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        friendService.deleteFriend(id, friendId);
    }
}
