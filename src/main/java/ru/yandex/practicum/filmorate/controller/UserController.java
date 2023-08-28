package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.NonExistentFilmException;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен запрос POST /users.");
        return userService.createUser(user);
    }

    @PutMapping
    public User replace(@RequestBody @Valid User user) {
        log.debug("Получен запрос PUT /users.");
        return userService.replaceUser(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentUserException("Параметр id должен быть положительным числом.");
        }
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}");
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        if (friendId == null) {
            throw new IncorrectIdException("Параметр friendId равен null.");
        }
        if (friendId <= 0) {
            throw new NonExistentUserException("Параметр friendId должен быть положительным числом.");
        }
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        if (friendId == null) {
            throw new IncorrectIdException("Параметр friendId равен null.");
        }
        if (friendId <= 0) {
            throw new NonExistentUserException("Параметр friendId должен быть положительным числом.");
        }
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findListFriends(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        return userService.findListFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (id == null) {
            throw new IncorrectIdException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new NonExistentFilmException("Параметр id должен быть положительным числом.");
        }
        if (otherId == null) {
            throw new IncorrectIdException("Параметр friendId равен null.");
        }
        if (otherId <= 0) {
            throw new NonExistentUserException("Параметр friendId должен быть положительным числом.");
        }
        return userService.findCommonFriends(id, otherId);
    }
}