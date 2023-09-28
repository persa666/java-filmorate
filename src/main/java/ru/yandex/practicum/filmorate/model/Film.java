package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.constraints.ReleaseDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;

    public Film(String name, String description, LocalDate releaseDate, Duration duration, Set<Integer> likes,
                Set<Genre> genresFilm, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.genres = genresFilm;
        this.mpa = mpa;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

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

    public void setGenre(List<Genre> list) {
        this.genres.addAll(list);
    }

    public void replaceGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void setLike(List<Integer> list) {
        this.likes.addAll(list);
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