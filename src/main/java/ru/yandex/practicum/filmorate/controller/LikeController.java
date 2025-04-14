package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.LikeService;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class LikeController {
    public static final String FILM_LIKE_PATH = "/{id}/like/{user-id}";

    private final LikeService likeService;

    @PutMapping(FILM_LIKE_PATH)
    public void addLike(@PathVariable Long id, @PathVariable("user-id") Long userId) {
        likeService.addLike(id, userId);
    }

    @DeleteMapping(FILM_LIKE_PATH)
    public void removeLike(@PathVariable Long id, @PathVariable("user-id") Long userId) {
        likeService.removeLike(id, userId);
    }
}
