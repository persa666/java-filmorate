package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.constraints.ReleaseDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotEmpty
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @ReleaseDate(after = "1895-12-19", message = "Дата должна быть позже 1895-12-19")
    private LocalDate releaseDate;
    @DurationMin(nanos = 0)
    private Duration duration;

    public long getDuration() {
        return this.duration.toMinutes();
    }

    public void setDuration(final long duration) {
        this.duration = Duration.ofMinutes(duration);
    }
}