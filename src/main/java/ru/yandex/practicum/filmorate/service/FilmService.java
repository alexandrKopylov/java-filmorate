package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage,   @Qualifier("userDbStorage")  UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Film getById(Long id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", id);
                    return new NotFoundValidationException("Фильм с таким id не найден");
                });
    }

    public void addLike(Long filmId, Long userId) {
        // Проверяем, существует ли фильм
        filmStorage.getById(filmId)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден при добавлении лайка", filmId);
                    return new NotFoundValidationException("Фильм с таким id не найден");
                });

        // Проверяем, существует ли пользователь
        userStorage.getById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден при добавлении лайка", userId);
                    return new NotFoundValidationException("Пользователь с таким id не найден");
                });

        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        // Проверяем, существует ли фильм
        filmStorage.getById(filmId)
                .orElseThrow(() -> {
                    log.error("Фильм с ID {} не найден при удалении лайка", filmId);
                    return new NotFoundValidationException("Фильм с таким id не найден");
                });

        userStorage.getById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден при удалении лайка", userId);
                    return new NotFoundValidationException("Пользователь с таким id не найден");
                });

        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть положительным");
        }
        return filmStorage.getPopular(count);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }
}