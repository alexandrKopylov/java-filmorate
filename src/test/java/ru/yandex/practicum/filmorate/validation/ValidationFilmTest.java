package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ValidationFilmTest {

    @Test
    public void shouldThrowExceptionWhenNameIsBlank() {
        Film film = new Film();
        film.setName("   ");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        assertThrows(ConditionsNotMetValidationException.class, () -> ValidationFilm.validation(film));
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionTooLong() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("a".repeat(201)); // 201 символ
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        assertThrows(ConditionsNotMetValidationException.class, () -> ValidationFilm.validation(film));
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateBefore1895() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27)); // раньше 28.12.1895
        film.setDuration(120);

        assertThrows(LocalDateValidationException.class, () -> ValidationFilm.validation(film));
    }

    @Test
    public void shouldThrowExceptionWhenDurationNegative() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(-1);

        assertThrows(ConditionsNotMetValidationException.class, () -> ValidationFilm.validation(film));
    }

    @Test
    public void shouldNotThrowExceptionForValidFilm() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Короткое описание");
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 28)); // ровно граница
        film.setDuration(0); // допустимо (ноль — не отрицательное)

        assertDoesNotThrow(() -> ValidationFilm.validation(film));
    }
}
