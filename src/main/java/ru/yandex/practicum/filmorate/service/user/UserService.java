package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NonExistentUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User replaceUser(User user) {
        return userStorage.replace(user);
    }

    public User findUserById(Integer id) {
        return userStorage.findById(id);
    }

    public User addToFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        userStorage.findById(friendId);
        user.addFriend(friendId);
        return userStorage.replace(user);
    }

    public User removeFromFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(friendId);
        if (user.getFriendsIds().contains(friendId)) {
            user.removeFriend(friendId);
            userFriend.removeFriend(id);
            userStorage.replace(userFriend);
            return userStorage.replace(user);
        } else {
            throw new NonExistentUserException("Пользователи не состоят в друзьях.");
        }
    }

    public List<User> findListFriends(Integer id) {
        List<User> userFriends = new ArrayList<>();
        User user = userStorage.findById(id);
        for (Integer elem : user.getFriendsIds()) {
            userFriends.add(userStorage.findById(elem));
        }
        return userFriends;
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.findById(id);
        User userFriend = userStorage.findById(otherId);
        if (user.getFriendsIds().isEmpty() || userFriend.getFriendsIds().isEmpty()) {
            return commonFriends;
        } else {
            for (Integer elemUser : user.getFriendsIds()) {
                for (Integer elemUserFriend : userFriend.getFriendsIds()) {
                    if (elemUser.equals(elemUserFriend)) {
                        commonFriends.add(userStorage.findById(elemUser));
                    }
                }
            }
        }
        return commonFriends;
    }
}