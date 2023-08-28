package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    public User create(User user) {
        user.setId(users.size() + 1);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User replace(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NonExistentUserException("Пользователя с таким id не существует.");
        } else {
            users.replace(user.getId(), user);
        }
        return user;
    }

    public User findById(Integer id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NonExistentUserException("Пользователя с таким id не существует."));
    }
}