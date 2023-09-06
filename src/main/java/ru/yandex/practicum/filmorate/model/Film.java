package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.constraints.ReleaseDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    private int id;
    @NotEmpty
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @ReleaseDate(after = "1895-12-19", message = "Дата должна быть позже 1895-12-19")
    private LocalDate releaseDate;
    @DurationMin(nanos = 0)
    private Duration duration;
    private Set<Integer> likes = new HashSet<>();

    public void putLike(int id) {
        likes.add(id);
    }

    public void removeLike(int id) {
        likes.remove(id);
    }

    public long getDuration() {
        return this.duration.toMinutes();
    }

    public void setDuration(final long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    @Override
    public int compareTo(Film film) {
        if (this.equals(film)) {
            return 0;
        }
        if (this.likes.size() - film.likes.size() < 0) {
            return 1;
        }
        if ((this.getLikes().size() - film.getLikes().size()) > 0) {
            return -1;
        } else {
            if ((this.getId() - film.getId()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}