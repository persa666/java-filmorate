package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public User(String name, String email, String login, LocalDate birthday, Set<Integer> friendsIds) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.friendsIds = friendsIds;
    }

    public void addFriend(int id) {
        friendsIds.add(id);
    }

    public void removeFriend(int id) {
        friendsIds.remove(id);
    }
}