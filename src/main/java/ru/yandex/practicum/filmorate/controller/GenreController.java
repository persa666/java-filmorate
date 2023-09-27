package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.NonExistentGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private static final Logger log = LoggerFactory.getLogger(GenreController.class);

    @GetMapping
    public List<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@PathVariable Integer id) {
        checkNumberForCorrect(id);
        return genreService.findGenreById(id);
    }

    private void checkNumberForCorrect(Integer id) {
        if (id == null) {
            throw new IncorrectIdException("Параметр равен null.");
        }
        if (id <= 0) {
            throw new NonExistentGenreException("Параметр должен быть положительным числом.");
        }
    }
}
