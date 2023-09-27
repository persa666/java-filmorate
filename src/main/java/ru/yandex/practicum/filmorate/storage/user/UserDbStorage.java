package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectUserException;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component("userDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        String sqlFromUsers = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlFromUsers, this::makeUser);
        String sqlFromFriends = "SELECT friend_id FROM friends WHERE user_id = ? AND friend_status = TRUE";
        for (User user : users) {
            List<Integer> friendsIds = jdbcTemplate.query(sqlFromFriends, this::makeFriendsIds, user.getId());
            for (Integer elem : friendsIds) {
                user.addFriend(elem);
            }
        }
        return users;
    }

    @Override
    public User create(User user) {
        try {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sqlToUser = "INSERT INTO users (user_name, user_email, user_login, user_birthday) " +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatementCreatorFactory pscf =
                                    new PreparedStatementCreatorFactory(
                                            sqlToUser,
                                            Types.VARCHAR,
                                            Types.VARCHAR,
                                            Types.VARCHAR,
                                            Types.DATE);
                            pscf.setReturnGeneratedKeys(true);
                            PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                                    Arrays.asList(user.getName(), user.getEmail(), user.getLogin(),
                                            user.getBirthday()));
                            return psc.createPreparedStatement(con);
                        }
                    }, keyHolder
            );
            user.setId((int) keyHolder.getKey());
            if (!user.getFriendsIds().isEmpty()) {
                String sqlFromFriends = "SELECT friend_status FROM friends WHERE user_id = ? AND friend_id = ? " +
                        "ON CONFLICT DO NOTHING";
                String sqlToFriends = "INSERT INTO friends (user_id, friend_id, friend_status) VALUES (?, ?, ?)";
                for (Integer elem : user.getFriendsIds()) {
                    User friend = findById(elem);
                    if (friend.getFriendsIds().contains(user.getId())) {
                        Boolean status = jdbcTemplate.queryForObject(sqlFromFriends, this::makeFriendStatus, elem,
                                user.getId());
                        if (!status) {
                            jdbcTemplate.update(sqlToFriends, user.getId(), elem, true);
                            String sqlToFriendsOld = "UPDATE friends SET friend_status = TRUE WHERE user_id = ?" +
                                    " AND friend_id = ?";
                            jdbcTemplate.update(sqlToFriendsOld, elem, user.getId());
                        }
                    }
                    jdbcTemplate.update(sqlToFriends, user.getId(), elem, true);
                }
            }
            return user;
        } catch (RuntimeException e) {
            throw new IncorrectUserException("Неверные поля у user.");
        }
    }

    @Override
    public User replace(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        try {
            User oldUser = findById(user.getId());
            if (!user.equals(oldUser)) {
                String sqlToUsers = "UPDATE users SET user_name = ?, user_email = ?, user_login = ?," +
                        " user_birthday = ? WHERE user_id = ?";
                jdbcTemplate.update(sqlToUsers, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(),
                        user.getId());
            }
            if (!oldUser.getFriendsIds().isEmpty()) {
                String sqlToFriendsOld = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
                for (Integer elem : oldUser.getFriendsIds()) {
                    jdbcTemplate.update(sqlToFriendsOld, oldUser.getId(), elem);
                }
            }
            if (!user.getFriendsIds().isEmpty()) {
                String sqlFromFriends = "SELECT friend_status FROM friends WHERE user_id = ? AND friend_id = ? " +
                        "ON CONFLICT DO NOTHING";
                String sqlToFriends = "INSERT INTO friends (user_id, friend_id, friend_status) VALUES (?, ?, ?)";
                for (Integer elem : user.getFriendsIds()) {
                    User friend = findById(elem);
                    if (friend.getFriendsIds().contains(user.getId())) {
                        Boolean status = jdbcTemplate.queryForObject(sqlFromFriends, this::makeFriendStatus, elem,
                                user.getId());
                        if (!status) {
                            jdbcTemplate.update(sqlToFriends, user.getId(), elem, true);
                            String sqlToFriendsOld = "UPDATE friends SET friend_status = TRUE WHERE user_id = ?" +
                                    " AND friend_id = ?";
                            jdbcTemplate.update(sqlToFriendsOld, elem, user.getId());
                        }
                    }
                    jdbcTemplate.update(sqlToFriends, user.getId(), elem, true);
                }
            }
            return findById(user.getId());
        } catch (RuntimeException e) {
            throw new NonExistentUserException("Такого пользоваотеля нет");
        }
    }

    @Override
    public User findById(Integer id) {
        try {
            String sqlFromUsers = "SELECT * FROM users WHERE user_id = ?";
            User user = jdbcTemplate.queryForObject(sqlFromUsers, this::makeUser, id);
            String sqlFromFriends = "SELECT friend_id FROM friends WHERE user_id = ?";
            List<Integer> friendsIds = jdbcTemplate.query(sqlFromFriends, this::makeFriendsIds, id);
            if (!friendsIds.isEmpty()) {
                for (Integer elem : friendsIds) {
                    user.addFriend(elem);
                }
            }
            return user;
        } catch (RuntimeException e) {
            throw new NonExistentUserException("Такого пользователя нет");
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder().id(rs.getInt("user_id"))
                .name(rs.getString("user_name"))
                .email(rs.getString("user_email"))
                .login(rs.getString("user_login"))
                .birthday(rs.getDate("user_birthday").toLocalDate())
                .friendsIds(new HashSet<>()).build();
    }

    private Integer makeFriendsIds(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("friend_id");
    }

    private Boolean makeFriendStatus(ResultSet rs, int rowNum) throws SQLException {
        return rs.getBoolean("friend_status");
    }
}