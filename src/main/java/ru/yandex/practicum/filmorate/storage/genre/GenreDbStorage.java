package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NonExistentGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAllGenres() {
        String sqlFromGenre = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlFromGenre, this::makeGenre);
    }

    @Override
    public Genre findGenreById(Integer id) {
        try {
            String sqlFromGenre = "SELECT * FROM genre WHERE genre_id = ?";
            return jdbcTemplate.queryForObject(sqlFromGenre, this::makeGenre, id);
        } catch (RuntimeException e) {
            throw new NonExistentGenreException("Жанра с таким id нет");
        }
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder().id(rs.getInt("genre_id"))
                .name(rs.getString("name")).build();
    }
}
