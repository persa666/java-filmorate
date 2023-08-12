package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> findAll() {
        List<User> currentUserList = new ArrayList<>();
        if (!users.isEmpty()) {
            currentUserList.addAll(users.values());
        }
        return currentUserList;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен запрос POST /users.");
        user.setId(users.size() + 1);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User replace(@RequestBody @Valid User user) {
        log.debug("Получен запрос PUT /users.");
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с таким id не существует");
        } else {
            users.replace(user.getId(), user);
        }
        return users.get(user.getId());
    }
}