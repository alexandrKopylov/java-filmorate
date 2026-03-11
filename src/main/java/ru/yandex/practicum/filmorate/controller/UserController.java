package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Utils;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    // Получение списка пользователей
    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен запрос на получение всех пользователей. Количество пользователей: {}", users.size());
        return users.values();
    }

    // Добавление нового пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        try {
            ValidationUser.validation(user);

            if (containsEmail(user.getEmail())) {
                log.warn("Попытка регистрации с уже существующим email: {}", user.getEmail());
                throw new DuplicatedDataValidationException("Этот имейл уже используется");
            }

            user.setId(Utils.getNextId(users.keySet()));
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь с id={}, email: '{}'", user.getId(), user.getEmail());
            return user;
        } catch (ConditionsNotMetValidationException | LoginValidationException | BirthdayValidationException e) {
            log.warn("Ошибка валидации при добавлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    // Обновление данных пользователя
    @PutMapping
    public User update(@RequestBody User newUser) {

        try {
            ValidationUser.validation(newUser);

            if (users.containsKey(newUser.getId())) {
                if (containsEmailAndId(newUser)) {
                    log.warn("Попытка обновления email на уже существующий для другого пользователя: {}", newUser.getEmail());
                    throw new DuplicatedDataValidationException("Этот имейл уже используется");
                }

                users.put(newUser.getId(), newUser);
                log.info("Обновлён пользователь с id={}, новый email: '{}'", newUser.getId(), newUser.getEmail());
                return newUser;
            } else {
                log.warn("Попытка обновления несуществующего пользователя с id={}", newUser.getId());
                throw new NotFoundValidationException("User с id = " + newUser.getId() + " не найден");
            }
        } catch (ConditionsNotMetValidationException | LoginValidationException | BirthdayValidationException e) {
            log.warn("Ошибка валидации при обновлении пользователя id={}: {}", newUser.getId(), e.getMessage());
            throw e;
        }
    }

    private boolean containsEmail(String email) {
        return users.values().stream()
                .map(User::getEmail)
                .anyMatch(x -> x.equals(email));
    }

    private boolean containsEmailAndId(User user) {
        return users.values().stream()
                .anyMatch(x -> x.getEmail().equals(user.getEmail()) && !x.getId().equals(user.getId()));
    }
}
