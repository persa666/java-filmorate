package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NonExistentMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor

public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAllMpa() {
        String sqlFromMpa = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlFromMpa, this::makeMpa);
    }

    @Override
    public Mpa findMpaById(Integer id) {
        try {
            String sqlFromMpa = "SELECT * FROM mpa WHERE mpa_id = ?";
            return jdbcTemplate.queryForObject(sqlFromMpa, this::makeMpa, id);
        } catch (RuntimeException e) {
            throw new NonExistentMpaException("Рейтинга с таким id нет");
        }
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder().id(rs.getInt("mpa_id"))
                .name(rs.getString("name")).build();
    }
}
