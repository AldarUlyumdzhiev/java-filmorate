package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("mpa")
    private MpaRating mpaRating;

    public long getDuration() {
        return duration.toMinutes();
    }

    public void setDuration(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }
}
