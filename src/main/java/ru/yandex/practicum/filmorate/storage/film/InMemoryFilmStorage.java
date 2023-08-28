package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll() {
        List<Film> currentFilmsList = new ArrayList<>();
        if (!films.isEmpty()) {
            currentFilmsList.addAll(films.values());
        }
        return currentFilmsList;
    }

    public Film create(Film film) {
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        return film;
    }

    public Film replace(Film film) {
        if (!films.containsKey(film.getId())) {
            return null;
        } else {
            films.replace(film.getId(), film);
        }
        return film;
    }

    public Film findFilmById(int id) {
        return films.getOrDefault(id, null);
    }
}
