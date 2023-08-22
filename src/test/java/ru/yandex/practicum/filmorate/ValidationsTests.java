package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class ValidationsTests {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void realeseDateValidatorNormalTest() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.parse("1800-01-01"));
        film.setDuration(90);
        Film film2 =
                testRestTemplate.postForEntity("http://localhost:8080/films", film, Film.class).getBody();
        film.setId(1);
        Assertions.assertNotEquals(film, film2);
    }

    @Test
    void realeseDateValidatorBadTest() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        film.setDuration(90);
        Film film2 =
                testRestTemplate.postForEntity("http://localhost:8080/films", film, Film.class).getBody();
        film.setId(1);
        Assertions.assertEquals(film, film2);
    }

    @Test
    void realeseDateValidatorBadTest2() {
        Film film2 =
                testRestTemplate.postForEntity("http://localhost:8080/films", null, Film.class).getBody();
        Assertions.assertNotEquals(null, film2);
    }
}