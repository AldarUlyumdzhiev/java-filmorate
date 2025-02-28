package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;


@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    // Возвращает duration в секундах
    public long getDuration() {
        return duration.getSeconds();
    }
}
