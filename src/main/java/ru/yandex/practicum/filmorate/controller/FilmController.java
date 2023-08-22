package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> findAll() {
        List<Film> currentFilmsList = new ArrayList<>();
        if (!films.isEmpty()) {
            currentFilmsList.addAll(films.values());
        }
        return currentFilmsList;
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен запрос POST /films.");
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film replace(@NotEmpty @RequestBody(required = false) @Valid Film film) {
        log.debug("Получен запрос PUT /films.");
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильма с таким id не существует");
        } else {
            films.replace(film.getId(), film);
        }
        return film;
    }
}