package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Utils;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetValidationException;
import ru.yandex.practicum.filmorate.exception.LocalDateValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    // Получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос на получение всех фильмов. Количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    // Добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        try {
            ValidationFilm.validation(film);
            film.setId(Utils.getNextId(films.keySet()));
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм с id={}, название: '{}'", film.getId(), film.getName());
            return film;
        } catch (ConditionsNotMetValidationException | LocalDateValidationException e) {
            log.warn("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    // Обновление фильма
    @PutMapping
    public Film update(@RequestBody Film newFilm) {

        try {
            ValidationFilm.validation(newFilm);

            if (films.containsKey(newFilm.getId())) {
                films.put(newFilm.getId(), newFilm);
                log.info("Обновлён фильм с id={}, новое название: '{}'", newFilm.getId(), newFilm.getName());
                return newFilm;
            } else {
                log.warn("Попытка обновления несуществующего фильма с id={}", newFilm.getId());
                throw new NotFoundValidationException("Фильм с id = " + newFilm.getId() + " не найден");
            }
        } catch (ConditionsNotMetValidationException | LocalDateValidationException e) {
            log.warn("Ошибка валидации при обновлении фильма id={}: {}", newFilm.getId(), e.getMessage());
            throw e;
        }
    }
}
