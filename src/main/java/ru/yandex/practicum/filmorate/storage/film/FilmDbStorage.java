package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectFilmException;
import ru.yandex.practicum.filmorate.exception.NonExistentFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component("filmDbStorage")
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, this::makeFilm);
        String sqlFromFilmGenre = "SELECT g.* FROM genre g INNER JOIN film_genre fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        String sqlFromMpa = "SELECT * FROM mpa WHERE mpa_id IN (SELECT film_mpa_id FROM films WHERE film_id = ?)";
        String sqlFromLikes = "SELECT user_id FROM users_likes_films WHERE film_id = ?";
        for (Film film : films) {
            film.setGenre(jdbcTemplate.query(sqlFromFilmGenre, this::makeGenre, film.getId()));
            film.setMpa(jdbcTemplate.queryForObject(sqlFromMpa, this::makeMpa, film.getId()));
            film.setLike(jdbcTemplate.query(sqlFromLikes, this::makeLikes, film.getId()));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sqlToFilms = "INSERT INTO films(film_name, film_description, film_release_date, film_duration," +
                    " film_mpa_id) VALUES(?, ?, ?, ?, ?)";
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatementCreatorFactory pscf =
                                    new PreparedStatementCreatorFactory(
                                            sqlToFilms,
                                            Types.VARCHAR,
                                            Types.VARCHAR,
                                            Types.DATE,
                                            Types.INTEGER,
                                            Types.INTEGER);
                            pscf.setReturnGeneratedKeys(true);

                            PreparedStatementCreator psc =
                                    pscf.newPreparedStatementCreator(
                                            Arrays.asList(film.getName(), film.getDescription(), film.getReleaseDate(),
                                                    film.getDuration(), film.getMpa().getId()));
                            return psc.createPreparedStatement(con);
                        }
                    }, keyHolder
            );
            film.setId((int) keyHolder.getKey());
            if (!film.getGenres().isEmpty()) {
                String sqlToFilmGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
                for (Genre elem : film.getGenres()) {
                    jdbcTemplate.update(sqlToFilmGenre, film.getId(), elem.getId());
                    String sqlFromGenre = "SELECT * FROM genre WHERE genre_id = ?";
                    film.replaceGenre(jdbcTemplate.queryForObject(sqlFromGenre, this::makeGenre, elem.getId()));
                    film.getGenres().remove(elem);
                }
            }
            if (film.getMpa() != null) {
                String sqlFromMpa = "SELECT * FROM mpa WHERE mpa_id = ?";
                film.setMpa(jdbcTemplate.queryForObject(sqlFromMpa, this::makeMpa, film.getMpa().getId()));
            }
            return film;
        } catch (RuntimeException e) {
            throw new IncorrectFilmException("Неверные поля у film.");
        }
    }

    @Override
    public Film replace(Film film) {
        try {
            Film oldFilm = findFilmById(film.getId());
            String sqlToFilm = "UPDATE films SET film_name = ?, film_description = ?, film_release_date " +
                    "= ?, film_duration = ?, film_mpa_id = ? WHERE film_id = ?";
            jdbcTemplate.update(sqlToFilm, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());

            if (!oldFilm.getGenres().isEmpty()) {
                String sqlToGenreOldFilm = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?";
                for (Genre elem : oldFilm.getGenres()) {
                    jdbcTemplate.update(sqlToGenreOldFilm, oldFilm.getId(), elem.getId());
                }
            }
            if (!film.getGenres().isEmpty()) {
                String sqlToFilmGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
                for (Genre elem : film.getGenres()) {
                    jdbcTemplate.update(sqlToFilmGenre, film.getId(), elem.getId());
                }
            }
            if (!oldFilm.getLikes().isEmpty()) {
                String sqlFromLikes = "DELETE FROM users_likes_films WHERE user_id = ? AND film_id = ?";
                for (Integer elem : oldFilm.getLikes()) {
                    jdbcTemplate.update(sqlFromLikes, elem, oldFilm.getId());
                }
            }
            if (!film.getLikes().isEmpty()) {
                String sqlToLikes = "INSERT INTO users_likes_films (user_id, film_id) VALUES (?, ?)";
                for (Integer elem : film.getLikes()) {
                    jdbcTemplate.update(sqlToLikes, elem, film.getId());
                }
            }
            return film = findFilmById(film.getId());
        } catch (RuntimeException e) {
            throw new NonExistentFilmException("Фильма с таким id нет");
        }
    }

    @Override
    public Film findFilmById(int id) {
        try {
            String sqlFromFilms = "SELECT * FROM films WHERE film_id = ?";
            Film film = jdbcTemplate.queryForObject(sqlFromFilms, this::makeFilm, id);
            String sqlFromFilmGenre = "SELECT g.* FROM genre g INNER JOIN film_genre fg ON g.genre_id = fg.genre_id " +
                    "WHERE fg.film_id = ?";
            film.setGenre(jdbcTemplate.query(sqlFromFilmGenre, this::makeGenre, id));
            String sqlFromMpa = "SELECT * FROM mpa WHERE mpa_id IN (SELECT film_mpa_id FROM films WHERE film_id = ?)";
            film.setMpa(jdbcTemplate.queryForObject(sqlFromMpa, this::makeMpa, id));

            String sqlFromLikes = "SELECT user_id FROM users_likes_films WHERE film_id = ?";
            film.setLike(jdbcTemplate.query(sqlFromLikes, this::makeLikes, id));
            return film;
        } catch (RuntimeException e) {
            throw new NonExistentFilmException("Фильма с таким id нет");
        }
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder().id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("film_release_date").toLocalDate())
                .duration(Duration.ofMinutes(rs.getInt("film_duration")))
                .likes(new HashSet<>())
                .genres(new HashSet<>())
                .mpa(new Mpa()).build();
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder().id(rs.getInt("genre_id"))
                .name(rs.getString("name")).build();
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder().id((rs.getInt("mpa_id")))
                .name(rs.getString("name")).build();
    }

    private Integer makeLikes(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt(rs.getInt("user_id"));
    }
}