package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    //    GET /users — для получения списка пользователей.
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }


    //    POST /users — для добавления нового пользователя в список.
    @PostMapping
    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (users.containsValue(user.getEmail())){
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId( getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    //    PUT /users — для обновления данных существующего пользователя.
    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {

            if (newUser.getEmail() == null ||  newUser.getUsername() == null || newUser.getPassword() == null){
                return  newUser;
            }
            if (users.containsValue(newUser.getEmail())){
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());

            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }
}

