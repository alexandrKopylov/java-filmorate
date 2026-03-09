package ru.yandex.practicum.filmorate.exception;

public class NotFoundValidationException extends ValidationException {
    public NotFoundValidationException(String message) {
        super(message);
    }
}