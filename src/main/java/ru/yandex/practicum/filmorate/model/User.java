package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private String name = "";
    private int id;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String login;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friendsIds = new HashSet<>();

    public void addFriend(int id) {
        friendsIds.add(id);
    }

    public void removeFriend(int id) {
        friendsIds.remove(id);
    }
}