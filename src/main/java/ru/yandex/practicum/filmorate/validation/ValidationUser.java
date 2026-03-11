package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.BirthdayValidationException;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetValidationException;
import ru.yandex.practicum.filmorate.exception.LoginValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidationUser {

    public static void validation(User user) {
        log.info("Начинается валидация пользователя с ID: {}", user.getId());



        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации: email некорректен или отсутствует. User ID: {}, Email: {}",
                    user.getId(), user.getEmail());
            throw new ConditionsNotMetValidationException("Имейл должен быть указан и содержать символ @");
        }
        log.debug("Проверка email пройдена. Email: {}", user.getEmail());

        if (user.getLogin().isBlank()) {
            log.error("Ошибка валидации: логин пуст. User ID: {}", user.getId());
            throw new LoginValidationException("Логин не может быть пустым");
        }
        log.debug("Проверка логина пройдена. Login: {}", user.getLogin());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Имя пользователя не указано, установлено значение логина. User ID: {}, Name: {}",
                    user.getId(), user.getName());
        }
        log.debug("Проверка имени пройдена. Name: {}", user.getName());

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: дата рождения в будущем. User ID: {}, Birthday: {}",
                    user.getId(), user.getBirthday());
            throw new BirthdayValidationException("Дата рождения не может быть в будущем");
        }
        log.debug("Проверка даты рождения пройдена. Birthday: {}", user.getBirthday());
        log.info("Валидация пользователя завершена успешно. User ID: {}", user.getId());
    }
}
