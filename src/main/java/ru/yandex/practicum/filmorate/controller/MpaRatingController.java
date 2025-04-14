package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRating> getAllRatings() {
        return mpaRatingService.getAll();
    }

    @GetMapping("/{id}")
    public MpaRating getRatingById(@PathVariable int id) {
        return mpaRatingService.getById(id);
    }
}
