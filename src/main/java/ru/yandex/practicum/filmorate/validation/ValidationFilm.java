package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetValidationException;
import ru.yandex.practicum.filmorate.exception.LocalDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class ValidationFilm {

    public static void validation(Film film) {
        log.info("Начинается валидация фильма с ID: {}", film.getId());

        if (film.getId() == null) {
            log.warn("Попытка обновления фильма без указания id");
            throw new ConditionsNotMetValidationException("Id должен быть указан");
        }

        if (film.getName().isBlank()) {
            log.error("Ошибка валидации: имя фильма не указано. Film ID: {}", film.getId());
            throw new ConditionsNotMetValidationException("Имя фильма должно быть указано");
        }
        log.debug("Проверка имени фильма пройдена. Name: {}", film.getName());

        if (film.getDescription().length() > 200) {
            log.error("Ошибка валидации: описание фильма превышает 200 символов. Film ID: {}, Length: {}",
                    film.getId(), film.getDescription().length());
            throw new ConditionsNotMetValidationException("Описание фильма не должно превышать 200 символов");
        }
        log.debug("Проверка длины описания пройдена. Length: {}", film.getDescription().length());

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Ошибка валидации: дата релиза раньше 28.12.1895. Film ID: {}, ReleaseDate: {}",
                    film.getId(), film.getReleaseDate());
            throw new LocalDateValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        log.debug("Проверка даты релиза пройдена. ReleaseDate: {}", film.getReleaseDate());

        if (film.getDuration() < 0) {
            log.error("Ошибка валидации: продолжительность фильма отрицательная. Film ID: {}, Duration: {}",
                    film.getId(), film.getDuration());
            throw new ConditionsNotMetValidationException("Продолжительность фильма должна быть положительным числом");
        }
        log.debug("Проверка продолжительности пройдена. Duration: {}", film.getDuration());
        log.info("Валидация фильма завершена успешно. Film ID: {}", film.getId());
    }
}
