package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        List<User> currentUserList = new ArrayList<>();
        if (!users.isEmpty()) {
            currentUserList.addAll(users.values());
        }
        return currentUserList;
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
            return null;
        } else {
            users.replace(user.getId(), user);
        }
        return users.get(user.getId());
    }

    public User findById(Integer id) {
        return users.getOrDefault(id, null);
    }
}