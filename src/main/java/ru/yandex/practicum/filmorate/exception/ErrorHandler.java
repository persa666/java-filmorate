package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCountOverflow(final IncorrectCountException e) {
        return new ErrorResponse(
                "Ошибка с параметром count.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIdOverflow(final IncorrectIdException e) {
        return new ErrorResponse(
                "Ошибка с параметром id.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(final NonExistentFilmException e) {
        return new ErrorResponse(
                "Ошибка с поиском фильма.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final NonExistentUserException e) {
        return new ErrorResponse(
                "Ошибка с поиском пользователя.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFound(final NonExistentGenreException e) {
        return new ErrorResponse(
                "Ошибка с поиском жанра.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMpaNotFound(final NonExistentMpaException e) {
        return new ErrorResponse(
                "Ошибка с поиском рейтинга.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserIncorrect(final IncorrectUserException e) {
        return new ErrorResponse(
                "Ошибка с созданием пользователя.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmIncorrect(final IncorrectFilmException e) {
        return new ErrorResponse(
                "Ошибка с созданием фильма.", e.getMessage()
        );
    }
}
