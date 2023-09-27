package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.NonExistentMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;
    private static final Logger log = LoggerFactory.getLogger(MpaController.class);

    @GetMapping
    public List<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable Integer id) {
        return mpaService.findMpaById(id);
    }

    private void checkNumberForCorrect(Integer id) {
        if (id == null) {
            throw new IncorrectIdException("Параметр равен null.");
        }
        if (id <= 0) {
            throw new NonExistentMpaException("Параметр должен быть положительным числом.");
        }
    }
}
