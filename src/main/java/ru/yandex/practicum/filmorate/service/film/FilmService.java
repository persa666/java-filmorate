package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.NonExistentFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film replace(Film film) {
        return filmStorage.replace(film);
    }

    public Film getFilmById(Integer id) {
        checkNumberForCorrect(id);
        return filmStorage.findFilmById(id);
    }

    public Film putLike(Integer id, Integer userId) {
        checkNumberForCorrect(id);
        checkNumberForCorrect(userId);
        Film film = filmStorage.findFilmById(id);
        userService.findUserById(userId);
        film.putLike(userId);
        return filmStorage.replace(film);
    }

    public Film removeLike(Integer id, Integer userId) {
        checkNumberForCorrect(id);
        checkNumberForCorrect(userId);
        Film film = filmStorage.findFilmById(id);
        userService.findUserById(userId);
        if (film.getLikes().contains(userId)) {
            film.removeLike(userId);
            return filmStorage.replace(film);
        } else {
            throw new IncorrectIdException("Пользователь с таким id не ставил лайк этому фильму.");
        }
    }

    public List<Film> findPopularFilms(String count) {
        List<Film> popularFilms = new ArrayList<>();
        Set<Film> sortPopularFilms = new TreeSet<>(filmStorage.findAll());
        if (count == null) {
            throw new IncorrectCountException("Параметр count равен null.");
        }
        if (count.isBlank()) {
            throw new IncorrectCountException("Параметр count пустой.");
        }
        try {
            int counter = Integer.parseInt(count);
            if (counter < 0) {
                throw new IncorrectCountException("Параметр count должен быть положительным числом.");
            }
            if (counter == 0) {
                if (!sortPopularFilms.isEmpty()) {
                    if (sortPopularFilms.size() <= 10) {
                        popularFilms.addAll(sortPopularFilms);
                    } else {
                        for (Film elem : sortPopularFilms) {
                            if (counter < 10) {
                                popularFilms.add(elem);
                                counter++;
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else {
                if (!sortPopularFilms.isEmpty()) {
                    if (sortPopularFilms.size() <= counter) {
                        popularFilms.addAll(sortPopularFilms);
                    } else {
                        for (Film elem : sortPopularFilms) {
                            if (counter > 0) {
                                popularFilms.add(elem);
                                counter--;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IncorrectCountException("Параметр count должен быть числом.");
        }
        return popularFilms;
    }

    private void checkNumberForCorrect(Integer id) {
        if (id == null) {
            throw new IncorrectIdException("Параметр равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр должен быть положительным числом.");
        }
    }
}
