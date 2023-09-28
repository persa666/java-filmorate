package ru.yandex.practicum.filmorate.exception;

public class NonExistentMpaException extends RuntimeException {
    public NonExistentMpaException(String message) {
        super(message);
    }
}
