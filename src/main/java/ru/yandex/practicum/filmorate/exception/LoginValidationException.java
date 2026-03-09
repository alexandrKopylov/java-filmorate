package ru.yandex.practicum.filmorate.exception;

public class LoginValidationException extends ValidationException{
    public LoginValidationException(String message) {
        super(message);
    }
}
