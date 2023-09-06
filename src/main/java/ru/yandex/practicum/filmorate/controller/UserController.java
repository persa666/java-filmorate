package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

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
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}");
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findListFriends(@PathVariable Integer id) {
        return userService.findListFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findCommonFriends(id, otherId);
    }
}