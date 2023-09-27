package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Test
    public void testFindUnknownUserById() {
        NonExistentUserException exception = assertThrows(NonExistentUserException.class,
                () -> {
                    userStorage.findById(500);
                });
        Assertions.assertEquals("Такого пользователя нет", exception.getMessage());
    }

    @Test
    public void testFindUserById() {
        userStorage.create(new User("Андрей", "yandex@maik.ru", "login123",
                LocalDate.parse("2000-01-01"), new HashSet<>()));
        Optional<User> userOptional = Optional.ofNullable(userStorage.findById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllUsers() {
        userStorage.create(new User("Андрей", "yandex@maik.ru", "login123",
                LocalDate.parse("2000-01-01"), new HashSet<>()));
        userStorage.create(new User("Олег", "google@maik.ru", "crystall",
                LocalDate.parse("1999-10-12"), new HashSet<>()));
        List<User> userList = userStorage.findAll();
        assertThat(userList)
                .isNotEmpty();
    }

    @Test
    public void testNormalCreateUser() {
        userStorage.create(new User("Андрей", "yandex@maik.ru", "login123",
                LocalDate.parse("2000-01-01"), new HashSet<>()));
        Optional<User> userOptional = Optional.ofNullable(userStorage.findById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("name", "Андрей")
                                .hasFieldOrPropertyWithValue("email", "yandex@maik.ru")
                                .hasFieldOrPropertyWithValue("login", "login123")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.parse("2000-01-01"))
                );
    }

    @Test
    public void testNotNormalCreateUser() {
        assertThrows(IncorrectUserException.class,
                () -> {
                    userStorage.create(new User());
                });
    }

    @Test
    public void testNormalReplaceUser() {
        User user = userStorage.create(new User("Андрей", "yandex@maik.ru", "login123",
                LocalDate.parse("2000-01-01"), new HashSet<>()));
        assertThat(user)
                .hasFieldOrPropertyWithValue("name", "Андрей")
                .hasFieldOrPropertyWithValue("email", "yandex@maik.ru");
        user.setName("Олег");
        user.setEmail("google@mail.com");
        assertThat(userStorage.replace(user))
                .hasFieldOrPropertyWithValue("name", "Олег")
                .hasFieldOrPropertyWithValue("email", "google@mail.com");
    }

    @Test
    public void testNotNormalReplaceUser() {
        assertThrows(NonExistentUserException.class,
                () -> {
                    userStorage.replace(new User("Андрей", "yandex@maik.ru", "login123",
                            LocalDate.parse("2000-01-01"), new HashSet<>()));
                });
    }

    @Test
    public void testFindUnknownFilmById() {
        NonExistentFilmException exception = assertThrows(NonExistentFilmException.class,
                () -> {
                    filmStorage.findFilmById(100);
                });
        Assertions.assertEquals("Фильма с таким id нет", exception.getMessage());
    }

    @Test
    public void testFindFilmById() {
        filmStorage.create(new Film("Фильм", "Описание", LocalDate.parse("1999-10-12"),
                Duration.ofMinutes(50), new HashSet<>(), new HashSet<>(), new Mpa(1, null)));
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllFilms() {
        filmStorage.create(new Film("Фильм", "Описание", LocalDate.parse("1999-10-12"),
                Duration.ofMinutes(50), new HashSet<>(), new HashSet<>(), new Mpa(1, null)));
        filmStorage.create(new Film("Фильм второй", "Описание", LocalDate.parse("2005-05-05"),
                Duration.ofMinutes(100), new HashSet<>(), new HashSet<>(), new Mpa(3, null)));
        List<Film> films = filmStorage.findAll();
        assertThat(films)
                .isNotEmpty();
    }

    @Test
    public void testNormalCreateFilm() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(new Film("Фильм2",
                "Описание", LocalDate.parse("1999-10-12"), Duration.ofMinutes(50),
                new HashSet<>(), new HashSet<>(), new Mpa(1, null))));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", filmOptional.get().getId())
                                .hasFieldOrPropertyWithValue("name", "Фильм2")
                );
    }

    @Test
    public void testNotNormalCreateFilm() {
        assertThrows(IncorrectFilmException.class,
                () -> {
                    filmStorage.create(new Film());
                });
    }

    @Test
    public void testNormalReplaceFilm() {
        Film film = filmStorage.create(new Film("Фильм3", "Описание", LocalDate.parse("1999-10-12"),
                Duration.ofMinutes(50), new HashSet<>(), new HashSet<>(), new Mpa(1, null)));
        assertThat(film)
                .hasFieldOrPropertyWithValue("name", "Фильм3")
                .hasFieldOrPropertyWithValue("description", "Описание");
        film.setName("Обновление");
        film.setDescription("Усталость");
        filmStorage.replace(film);
        assertThat(film)
                .hasFieldOrPropertyWithValue("name", "Обновление")
                .hasFieldOrPropertyWithValue("description", "Усталость");
    }

    @Test
    public void testNotNormalReplaceFilm() {
        assertThrows(NonExistentFilmException.class,
                () -> {
                    filmStorage.replace(new Film("Фильм20", "Описание",
                            LocalDate.parse("1999-10-12"), Duration.ofMinutes(50), new HashSet<>(),
                            new HashSet<>(), new Mpa(1, null)));
                });
    }

    @Test
    public void testFindAllMpa() {
        List<Mpa> mpa = mpaStorage.findAllMpa();
        assertThat(mpa)
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    public void testFindUnknownMpaById() {
        assertThrows(NonExistentMpaException.class,
                () -> {
                    mpaStorage.findMpaById(999);
                });
    }

    @Test
    public void testFindMpaById() {
        assertThat(mpaStorage.findMpaById(1))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testFindAllGenres() {
        List<Genre> genres = genreStorage.findAllGenres();
        assertThat(genres)
                .isNotEmpty()
                .hasSize(6);
    }

    @Test
    public void testFindUnknownGenreById() {
        assertThrows(NonExistentGenreException.class,
                () -> {
                    genreStorage.findGenreById(999);
                });
    }

    @Test
    public void testFindGenreById() {
        assertThat(genreStorage.findGenreById(1))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }
}