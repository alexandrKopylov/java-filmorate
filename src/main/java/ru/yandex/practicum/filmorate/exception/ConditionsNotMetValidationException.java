package ru.yandex.practicum.filmorate.exception;


public class ConditionsNotMetValidationException extends ValidationException {
    public ConditionsNotMetValidationException(String message) {
        super(message);
    }
}
