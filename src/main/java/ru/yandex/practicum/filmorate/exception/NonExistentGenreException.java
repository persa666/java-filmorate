package ru.yandex.practicum.filmorate.exception;

public class NonExistentGenreException extends RuntimeException {
    public NonExistentGenreException(String message) {
        super(message);
    }
}
