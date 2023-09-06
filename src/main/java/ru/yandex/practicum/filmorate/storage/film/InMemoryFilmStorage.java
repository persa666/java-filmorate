package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NonExistentFilmException;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll() {
        return List.copyOf(films.values());
    }

    public Film create(Film film) {
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        return film;
    }

    public Film replace(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NonExistentUserException("Фильма с таким id не существует.");
        } else {
            films.replace(film.getId(), film);
        }
        return film;
    }

    public Film findFilmById(int id) {
        return Optional.ofNullable(films.get(id))
                .orElseThrow(() -> new NonExistentFilmException("Фильма с таким id не существует."));
    }
}
