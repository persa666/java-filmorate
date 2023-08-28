package ru.yandex.practicum.filmorate.exception;

public class NonExistentUserException extends RuntimeException {
    public NonExistentUserException(String message) {
        super(message);
    }
}
