package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikeService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmLikeController {
    private final FilmLikeService filmLikeService;
    private static final String LIKE_PATH = "/{id}/like/{user-id}";

    @Autowired
    public FilmLikeController(FilmLikeService filmLikeService) {
        this.filmLikeService = filmLikeService;
    }

    @PutMapping(LIKE_PATH)
    public void addLike(@PathVariable Long id, @PathVariable("user-id") Long userId) {
        filmLikeService.addLike(id, userId);
    }

    @DeleteMapping(LIKE_PATH)
    public void removeLike(@PathVariable Long id, @PathVariable("user-id") Long userId) {
        filmLikeService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmLikeService.getPopularFilms(count);
    }
}
