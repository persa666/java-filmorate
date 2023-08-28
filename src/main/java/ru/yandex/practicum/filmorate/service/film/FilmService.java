package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.NonExistentFilmException;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film replace(Film film) {
        Film replaceFilm = filmStorage.replace(film);
        if (replaceFilm == null) {
            throw new NonExistentFilmException("Фильма с таким id не существует.");
        }
        return replaceFilm;
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.findFilmById(id);
        if (film == null) {
            throw new NonExistentFilmException("Фильма с таким id не существует.");
        } else {
            return film;
        }
    }

    public Film putLike(int id, int userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userService.findUserById(userId);
        if (film == null) {
            throw new NonExistentFilmException("Фильма с таким id не существует.");
        }
        if (user == null) {
            throw new NonExistentUserException("Пользователя с таким id не существует.");
        }
        film.putLike(userId);
        return filmStorage.replace(film);
    }

    public Film removeLike(int id, int userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userService.findUserById(userId);
        if (film == null) {
            throw new NonExistentFilmException("Фильма с таким id не существует.");
        }
        if (user == null) {
            throw new NonExistentUserException("Пользователя с таким id не существует.");
        }
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
}
