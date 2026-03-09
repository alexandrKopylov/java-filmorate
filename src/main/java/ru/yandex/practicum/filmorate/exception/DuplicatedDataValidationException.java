package ru.yandex.practicum.filmorate.exception;

public class DuplicatedDataValidationException  extends ValidationException {
    public DuplicatedDataValidationException (String message) {
        super(message);
    }
}
