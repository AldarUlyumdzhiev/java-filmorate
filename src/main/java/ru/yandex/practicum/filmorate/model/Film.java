package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Genre> genres;
    private MpaRating mpa;

    public long getDuration() {
        return duration.getSeconds();
    }

    public void setDuration(long seconds) {
        this.duration = Duration.ofSeconds(seconds);
    }
}
