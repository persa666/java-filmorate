package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User replaceUser(User user) {
        User replaceUser = userStorage.replace(user);
        if (replaceUser == null) {
            throw new NonExistentUserException("Пользователя с таким id не существует.");
        }
        return replaceUser;
    }

    public User findUserById(int id) {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new NonExistentUserException("Пользователя с таким id не существует");
        } else {
            return user;
        }
    }

    public User addToFriends(int id, int friendId) {
        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(friendId);
        if (user == null) {
            throw new NonExistentUserException("Пользователя user с таким id не существует.");
        }
        if (userFriend == null) {
            throw new NonExistentUserException("Пользователя friend с таким friendId не существует.");
        }
        user.addFriend(friendId);
        userFriend.addFriend(id);
        userStorage.replace(userFriend);
        return userStorage.replace(user);
    }

    public User removeFromFriends(int id, int friendId) {
        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(friendId);
        if (user == null) {
            throw new NonExistentUserException("Пользователя user с таким id не существует.");
        }
        if (userFriend == null) {
            throw new NonExistentUserException("Пользователя friend с таким friendId не существует.");
        }
        if (user.getFriends().contains(friendId)) {
            user.removeFriend(friendId);
            userFriend.removeFriend(id);
            userStorage.replace(userFriend);
            return userStorage.replace(user);
        } else {
            throw new NonExistentUserException("Пользователи не состоят в друзьях.");
        }
    }

    public List<User> findListFriends(int id) {
        List<User> userFriends = new ArrayList<>();
        User user = userStorage.findById(id);
        if (user != null) {
            for (Integer elem : user.getFriends()) {
                userFriends.add(userStorage.findById(elem));
            }
        } else {
            throw new NonExistentUserException("Пользователя с таким id не существует.");
        }
        return userFriends;
    }

    public List<User> findCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(otherId);
        if (user == null) {
            throw new NonExistentUserException("Пользователя user с таким id не существует.");
        }
        if (userFriend == null) {
            throw new NonExistentUserException("Пользователя friend с таким friendId не существует.");
        }
        if (user.getFriends().isEmpty() || userFriend.getFriends().isEmpty()) {
            return commonFriends;
        } else {
            for (Integer elemUser : user.getFriends()) {
                for (Integer elemUserFriend : userFriend.getFriends()) {
                    if (elemUser.equals(elemUserFriend)) {
                        commonFriends.add(userStorage.findById(elemUser));
                    }
                }
            }
        }
        return commonFriends;
    }
}