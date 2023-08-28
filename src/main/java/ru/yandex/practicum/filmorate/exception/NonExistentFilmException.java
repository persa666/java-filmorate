package ru.yandex.practicum.filmorate.exception;

public class NonExistentFilmException extends RuntimeException {
    public NonExistentFilmException(String message) {
        super(message);
    }
}
