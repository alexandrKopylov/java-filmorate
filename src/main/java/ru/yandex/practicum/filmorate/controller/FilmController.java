package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Utils;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetValidationException;
import ru.yandex.practicum.filmorate.exception.LocalDateValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    // Получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос на получение всех фильмов. Количество фильмов: {}", films.size());
        return films.values();
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
        if (newFilm.getId() == null) {
            log.warn("Попытка обновления фильма без указания id");
            throw new ConditionsNotMetValidationException("Id должен быть указан");
        }

        try {
            ValidationFilm.validation(newFilm);

            if (films.containsKey(newFilm.getId())) {
                Film oldFilm = films.get(newFilm.getId());
                oldFilm.setDescription(newFilm.getDescription());
                oldFilm.setDuration(newFilm.getDuration());
                oldFilm.setName(newFilm.getName());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                log.info("Обновлён фильм с id={}, новое название: '{}'", newFilm.getId(), newFilm.getName());
                return oldFilm;
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
