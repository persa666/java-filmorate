package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.NonExistentFilmException;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен запрос POST /films.");
        return filmService.create(film);
    }

    @PutMapping
    public Film replace(@NotEmpty @RequestBody(required = false) @Valid Film film) {
        log.debug("Получен запрос PUT /films.");
        return filmService.replace(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Получен запрос PUT /films/{id}/like/{userId}.");
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        if (userId == null) {
            throw new IncorrectIdException("Параметр userId равен null.");
        }
        if (userId <= 0) {
            throw new NonExistentUserException("Параметр userId должен быть положительным числом.");
        }
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        if (userId == null) {
            throw new IncorrectIdException("Параметр userId равен null.");
        }
        if (userId <= 0) {
            throw new NonExistentUserException("Параметр userId должен быть положительным числом.");
        }
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false, defaultValue = "0") String count) {
        if (count == null) {
            throw new IncorrectCountException("Параметр count равен null.");
        }
        if (count.isBlank()) {
            throw new IncorrectCountException("Параметр count пустой.");
        }
        return filmService.findPopularFilms(count);
    }
}